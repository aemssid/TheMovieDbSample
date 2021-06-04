package certif.id.app.base

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.savvologytask.BR
import com.example.savvologytask.constant.Constant

abstract class BaseActivity<VM : BaseViewModel, VDB : ViewDataBinding>(@LayoutRes private val layoutId: Int) :
    AppCompatActivity() {

    abstract val viewModel : VM
    protected lateinit var binding : VDB


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,layoutId)
        binding.lifecycleOwner = this
        binding.setVariable(BR._all, viewModel)
    }

    fun <T : Any> launch(classType: Class<T>) {
        var intent: Intent = Intent(this, classType)
        intent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
        startActivity(intent)
    }


    fun launch(intent: Intent){
        intent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
        startActivity(intent)
    }

    fun launchWithClearBackstack(intent: Intent){
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    fun launchWithData(intent: Intent, hasAnimation: Boolean = false) {
        intent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
        if(hasAnimation){
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        }
        startActivity(intent)
    }

    fun launchForResult(intent: Intent, requestCode : Int){
        intent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
        startActivityForResult(intent,requestCode)
    }

    fun <T : Any> launchWithData(classType: Class<T>,bundle: Bundle, hasAnimation: Boolean = false) {
        var intent: Intent = Intent(this, classType)
        intent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
        intent.putExtra(Constant.KEY_BUNDLE, bundle)
        if(hasAnimation){
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        }
        startActivity(intent)
    }

    fun <T : Any> launchForResult(classType : Class<T>, requestCode : Int){
        var intent: Intent = Intent(this, classType)
        intent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
        startActivityForResult(intent,requestCode)
    }

    fun launchForResult(intent: Intent, bundle: Bundle,requestCode : Int){
        intent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
        intent.putExtra(Constant.KEY_BUNDLE, bundle)
        startActivityForResult(intent,requestCode)
    }

    fun <T : Any> launchForResult(classType : Class<T>,bundle: Bundle,requestCode : Int){
        var intent: Intent = Intent(this, classType)
        intent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
        intent.putExtra(Constant.KEY_BUNDLE, bundle)
        startActivityForResult(intent,requestCode)
    }

    fun launchwithFirstScreen(intent: Intent){
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    fun <T : Any> launchwithFirstScreen(classType: Class<T>) {
        var intent: Intent = Intent(this, classType)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }


    fun <T : Any> launchwithDataFirstScreen(classType: Class<T>, bundle: Bundle) {
        var intent: Intent = Intent(this, classType)
        intent.putExtra(Constant.KEY_BUNDLE, bundle)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
        finish()
    }

    fun <T : Any> launchWithClearBackstackPlusData(classType: Class<T>,bundle: Bundle) {
        var intent: Intent = Intent(this, classType)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.putExtra(Constant.KEY_BUNDLE, bundle)
        startActivity(intent)
        finish()
    }


    fun <T : Any> launchWithDataClear(classType: Class<T>, bundle: Bundle) {
        var intent: android.content.Intent = android.content.Intent(this, classType)
        intent.putExtra(Constant.KEY_BUNDLE, bundle)
        val cn = intent.component
        val mainIntent = Intent.makeRestartActivityTask(cn)
        mainIntent.putExtra(Constant.KEY_BUNDLE, bundle)
        startActivity(mainIntent)
        finish()
    }
    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    /*fun showSettingsDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.need_permission_title))
        builder.setMessage(getString(R.string.need_permission_msg))
        builder.setPositiveButton(getString(R.string.goto_setttings_cap), DialogInterface.OnClickListener { dialog, which ->
            dialog.cancel()
            openSettings()
        })
        builder.setNegativeButton(getString(R.string.cancel), DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.show()
    }*/


    fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    fun showProgressDialog() {
        //Utils.showProgressDialog(this)
    }

    fun hideProgressDialog() {
        //Utils.cancelProgressDialog()
    }

    override fun onPause() {
        super.onPause()
        /*hideProgressDialog()*/
    }

}

