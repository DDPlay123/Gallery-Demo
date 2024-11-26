package mai.project.galleryapp

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import mai.project.galleryapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    /**
     * 導航控制器
     */
    private lateinit var navController: NavController

    /**
     * 導航基底 Fragment
     */
    private lateinit var navHostFragment: NavHostFragment

    /**
     * 記錄返回鍵按下時間
     */
    private var backPressedTime: Long = 0

    /**
     * 顯示 SnackBar
     */
    fun showSnackBar(
        message: String,
        actionText: String,
        duration: Int = Snackbar.LENGTH_SHORT,
        doSomething: (Snackbar) -> Unit = {}
    ) = with(Snackbar.make(binding.root, message, duration)) {
        if (actionText.isNotEmpty()) {
            setAction(actionText) {
                doSomething.invoke(this)
                dismiss()
            }
        }
        setTextMaxLines(5)
        this.anchorView = anchorView
        animationMode = Snackbar.ANIMATION_MODE_FADE
        show()
    }

    /**
     * 顯示/隱藏 BottomBar
     */
    fun showBottomBar(
        isShow: Boolean
    ) {
        binding.bottomNavigation.isVisible = isShow
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        setupWindowPadding()
        doInitialized()
        setBackPress()
    }

    /**
     * 設定 Window 的間距
     */
    private fun setupWindowPadding() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    /**
     * 執行初始化
     */
    private fun doInitialized() {
        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navigationHost) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)
    }

    /**
     * 設定返回鍵事件
     *
     * 讓使用者在 3 秒內按下兩次返回鍵才能退出程式
     */
    private fun setBackPress() {
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (backPressedTime + 3000 > System.currentTimeMillis()) {
                        finish()
                    } else {
                        Snackbar.make(binding.root, getString(R.string.press_again_to_exit), Snackbar.LENGTH_SHORT).show()
                    }

                    backPressedTime = System.currentTimeMillis()
                }
            }
        )
    }
}