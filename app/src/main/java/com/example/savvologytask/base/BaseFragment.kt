package certif.id.app.base

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import org.koin.core.component.KoinApiExtension

abstract class BaseFragment<VM : BaseViewModel, VDB : ViewDataBinding>(@LayoutRes private val layoutId: Int) :
    Fragment() {

    abstract val viewModel: VM
    abstract fun setViewModel()
    protected lateinit var binding: VDB

    var actionOnPermission: ((granted: Int) -> Unit)? = null
    var isAskingPermissions = false
    private val GENERIC_PERM_HANDLER = 100

    var bindingView: View? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        activity?.let { (it as AppCompatActivity).changeStatusBarColor(R.color.colorWhite, true) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(bindingView == null) {
            binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
            binding.lifecycleOwner = this
            setViewModel()
            bindingView = binding.root
        }
        return bindingView
    }

    abstract fun initUi()
    abstract fun setListeners()

    fun launch(intent: Intent) {
        intent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
        startActivity(intent)
    }

    fun launchwithFirstScreen(intent: Intent){
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        activity?.finish()
    }

    fun launchWithClearBackstack(intent: Intent) {
        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        activity?.finish()
    }


    fun launchWithData(intent: Intent, hasAnimation: Boolean = false) {
        intent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
        if (hasAnimation) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        }
        startActivity(intent)
    }

    fun launchWithClearBackstack(intent: Intent, activity: FragmentActivity?) {
        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        activity?.finish()
    }

    fun showProgressDialog() {
        //context?.let { Utils.showProgressDialog(it) }
    }

    fun hideProgressDialog() {
        //context?.let { Utils.cancelProgressDialog() }
    }


    fun toast(message : String) {
        context?.let {
            Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
        }

    }
}