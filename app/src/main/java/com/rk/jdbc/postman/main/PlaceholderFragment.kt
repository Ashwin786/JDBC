package com.rk.jdbc.postman.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.support.v4.app.Fragment
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.rk.jdbc.R
import com.rk.jdbc.postman.data.apiService.ResultUpdatable
import com.rk.jdbc.postman.data.model.ApiDbDto
import com.rk.jdbc.postman.data.model.JsonDto
import kotlinx.android.synthetic.main.fragment_postman.*

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment(), ResultUpdatable {


    var jsondto = JsonDto()

    private var mContext: Context? = null
    private lateinit var tv_response: EditText
    private lateinit var pageViewModel: PageViewModel
    lateinit var ed_request: EditText
    lateinit var ed_url: EditText
    var what = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("PlaceholderFragment", "onCreate")
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)

        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.mContext = context
        Log.e("PlaceholderFragment", "onAttach")
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_postman, container, false)
        initView(root)
        return root
    }

    fun initView(root: View) {
        what = 2
        tv_response = root.findViewById(R.id.tv_response)
        ed_request = root.findViewById(R.id.ed_request)
        ed_url = root.findViewById(R.id.ed_url)

        val btn_send: Button = root.findViewById(R.id.btn_send)

//        jsondto.request.url = "http://testfps1.hpepds.com/login/validateuser"
           /*if (pageViewModel.tabCount == 1) {
               jsondto.request.url = "https://fps.hpepds.com/login/encrypted/validateuser"
               jsondto.request.body.raw = "{\"deviceId\":\"U2420161106026\",\"macId\":\"00:08:22:51:28:51\",\"password\":\"Alluser@123\",\"userName\":\"admin\",\"appType\":\"FPS\",\"statusCode\":2000}"
           }


        ed_url.setText(jsondto.request.url)
        ed_request.setText(jsondto.request.body.raw)*/

//        pageViewModel.text.observe(this, Observer<String> {
//            textView.text = it
//        })
        btn_send.setOnClickListener {
            pageViewModel.sendRequest(ed_url.text.toString(), ed_request.text.toString(), this, mContext,rd_encryption.isChecked)

        }
    }

    fun updateView(apiDbDto: ApiDbDto) {
        if (apiDbDto != null) {
            ed_url.setText(apiDbDto.url)
            ed_request.setText(apiDbDto.request)
        }
    }

    override fun onDetach() {
        super.onDetach()
        Log.e("PlaceholderFragment", "onDetach")
    }
    override fun setResult(result: String?) {

        jsondto.response = result!!.replace(",\"",",\n\"")
        Log.e("encryption response ", jsondto.response)
//        SessionId.getInstance().sessionid = Gson().fromJson<SessionId>(result, SessionId::class.java).sessionid
        tv_response.setText(jsondto.response)
    }


    override fun setErrorResponse(errorResponse: String?) {
        tv_response.setText(errorResponse)
    }

    companion object {
        private var fragment: PlaceholderFragment? = null
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): PlaceholderFragment {

            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}