package com.example.diplomaproject

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.diplomaproject.data.model.ModelVersions
import com.example.diplomaproject.data.services.DataPreparationService
import com.example.diplomaproject.data.services.spam.SpamDetectionService
import com.example.diplomaproject.ui.components.about.AdditionalItemInfo
import com.example.diplomaproject.ui.components.common.BottomNavigationBar
import com.example.diplomaproject.ui.components.common.BottomNavigationItem
import com.example.diplomaproject.ui.components.home.FeatureItem
import com.example.diplomaproject.ui.screens.AboutAppInfo
import com.example.diplomaproject.ui.screens.AboutScreen
import com.example.diplomaproject.ui.screens.AdditionalListInfo
import com.example.diplomaproject.ui.screens.FeaturesListState
import com.example.diplomaproject.ui.screens.HomeScreen
import com.example.diplomaproject.ui.screens.aboutAppInfoMock
import com.example.diplomaproject.ui.screens.additionalListInfoMock
import com.example.diplomaproject.ui.screens.destinations.AboutAppDestination
import com.example.diplomaproject.ui.screens.destinations.HomeDestination
import com.example.diplomaproject.ui.screens.featureStateMock
import com.example.diplomaproject.ui.screens.switchFunctionStateMock
import com.example.diplomaproject.ui.theme.DiplomaProjectTheme
import com.example.diplomaproject.ui.theme.TextPrimary


internal class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
        checkAccessibility()
    }

    override fun onResume() {
        super.onResume()
        checkAccessibility()
    }

    private fun checkAccessibility() {
        var accessEnabled = 0
        try {
            accessEnabled = Settings.Secure.getInt(contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED)
        } catch (e: Throwable) {
            Log.e(MainActivity::class.java.simpleName, "Cannot get accessibility state", e)
        }

        if (accessEnabled == 1) return
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun setupUI() {
        enableEdgeToEdge()
        setStatusBarColor()
        setContent {
            DiplomaProjectTheme {
                AppUi()
            }
        }
    }

    private fun setStatusBarColor() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.background_primary)
    }
}

@Composable
private fun AppUi() {
    val navController = rememberNavController()
    val homeTitle = stringResource(R.string.home_title_section)
    val aboutAppTitle = stringResource(R.string.about_app_title)
    val sections = remember {
        mutableStateListOf(
            BottomNavigationItem(
                icon = R.drawable.ic_home_default,
                label = homeTitle,
                isSelected = true,
                destination = HomeDestination
            ),
            BottomNavigationItem(
                icon = android.R.drawable.ic_dialog_info,
                label = aboutAppTitle,
                isSelected = false,
                destination = AboutAppDestination
            )
        )
    }
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                sections = sections.toList(),
                onSelect = { index, destination ->
                    sections.replaceAll { it.copy(isSelected = false) }
                    sections[index] = sections[index].copy(isSelected = true)
                    navController.navigate(destination)
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = HomeDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<HomeDestination> {
                val featuresTitles = stringArrayResource(R.array.features_titles)
                val featuresDescriptions = stringArrayResource(R.array.features_descriptions)
                val features = featuresTitles.zip(featuresDescriptions).map {
                    FeatureItem(
                        icon = R.drawable.ic_shield_default,
                        title = it.first,
                        description = it.second
                    )
                }
                HomeScreen(
                    featuresState = FeaturesListState(
                        title = stringResource(R.string.features_title),
                        titleColor = TextPrimary,
                        features = features
                    ),
                    switchFunctionState = switchFunctionStateMock
                )
            }
            composable<AboutAppDestination> {
                AboutScreen(
                    aboutAppInfo = AboutAppInfo(
                        title = stringResource(R.string.about_app_title),
                        description = stringResource(R.string.about_app_description),
                        appInfo = FeatureItem(
                            title = stringResource(R.string.app_info_title),
                            description = stringResource(R.string.app_version_subtitle),
                            icon = R.drawable.ic_shield_default
                        )
                    ),
                    additionalListInfo = AdditionalListInfo(
                        title = stringResource(R.string.additional_info_title),
                        additionalItemInfos = listOf(
                            AdditionalItemInfo(text = stringResource(R.string.licensing_item)),
                            AdditionalItemInfo(text = stringResource(R.string.permissions_item)),
                            AdditionalItemInfo(text = stringResource(R.string.accessibility_item)),
                        )
                    )
                )
            }
        }
    }
}


@Preview
@Composable
fun AppPreview(){
    DiplomaProjectTheme {
        AppUi()
    }
}