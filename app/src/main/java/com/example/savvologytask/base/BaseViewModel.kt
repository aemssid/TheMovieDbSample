package certif.id.app.base


import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import org.json.JSONObject
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import retrofit2.HttpException

abstract class BaseViewModel : ViewModel(), KoinComponent {

}