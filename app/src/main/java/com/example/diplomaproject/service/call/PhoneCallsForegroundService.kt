package com.example.diplomaproject.service.call

import android.content.ContentValues
import android.content.Intent
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.example.diplomaproject.R
import com.example.diplomaproject.service.AnalyzingForegroundService
import com.example.diplomaproject.service.utils.logi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PhoneCallsForegroundService : AnalyzingForegroundService<String>() {
    override val channelId = "${PhoneCallsForegroundService::class.simpleName}Channel"
    override val title get() = getString(R.string.calls_service_notification_title)
    override val description get() = getString(R.string.calls_service_notification_description)

    private var recorder: MediaRecorder? = null

    override fun Intent.prepareForAnalyzing(): String =
        getStringExtra("CALLER_PHONE_NUMBER") ?: throw RuntimeException("Unable to retrieve number")

    override fun analyze(dataProvider: () -> String) {
        logi<PhoneCallsForegroundService> { "The number is ${dataProvider()} " }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return
        startRecording(dataProvider())
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRecording()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun startRecording(contact: String?) {
        try {
            val timestamp: String = SimpleDateFormat(
                "dd-MM-yyyy-hh-mm-ss",
                Locale.getDefault()
            ).format(Date())

            val fileName = "${contact}_{$timestamp}.m4a"

            val contentValues = ContentValues().apply {
                put(MediaStore.Audio.Media.DISPLAY_NAME, fileName)
                put(MediaStore.Audio.Media.MIME_TYPE, "audio/m4a")
                put(
                    MediaStore.Audio.Media.RELATIVE_PATH,
                    Environment.DIRECTORY_MUSIC + "/MyAppRecordings"
                )
            }

            val resolver = baseContext.contentResolver
            val audioUri =
                resolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues)
                    ?: throw IOException("Cannot create MediaStore entry")

            // Шаг 2: Получить дескриптор файла и передать его MediaRecorder

            val pfd = resolver.openFileDescriptor(audioUri, "w") ?: throw IOException("Cannot open file")

            recorder = MediaRecorder(baseContext)
            recorder?.reset()

            //android.permission.MODIFY_AUDIO_SETTINGS
            val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager //turn on speaker
            audioManager.setCommunicationDevice(
                audioManager.availableCommunicationDevices.first { it.type == AudioDeviceInfo.TYPE_BUILTIN_SPEAKER }
            )

            // mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL), 0); // increase Volume
            hasWiredHeadset(audioManager)

            //android.permission.RECORD_AUDIO
            val manufacturer = Build.MANUFACTURER
            logi<PhoneCallsForegroundService> { " Current phone manufacturer is $manufacturer" }

            recorder?.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC) //MIC | VOICE_COMMUNICATION (Android 10 release) | VOICE_RECOGNITION | (VOICE_CALL = VOICE_UPLINK + VOICE_DOWNLINK)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4) //THREE_GPP | MPEG_4
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC) //AMR_NB | AAC
                setOutputFile(pfd.fileDescriptor)
                prepare()
                start()
            }

            coroutineScope.launch {
                delay(5000)
                stopRecording()
                pfd.close()
            }

            //String selectedPath = Environment.getExternalStorageDirectory() + "/Testing";
            //String selectedPath = Environment.getExternalStorageDirectory().getAbsolutePath() +"/Android/data/" + packageName + "/system_sound";

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun stopRecording() {
        recorder?.stop()
        recorder?.release()
        recorder = null
    }

    // To detect the connected other device like headphone, wifi headphone, usb headphone etc
    private fun hasWiredHeadset(audioManager: AudioManager): Boolean {
        var result = false
        buildList<AudioDeviceInfo> {
            addAll(audioManager.getDevices(AudioManager.GET_DEVICES_INPUTS))
            addAll(audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS))
        }.forEach {
            when (it.type) {
                AudioDeviceInfo.TYPE_WIRED_HEADSET,
                AudioDeviceInfo.TYPE_USB_DEVICE,
                AudioDeviceInfo.TYPE_TELEPHONY -> result = true

                else -> Unit
            }
        }

        return result
    }
}