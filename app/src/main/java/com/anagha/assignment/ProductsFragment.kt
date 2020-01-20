package com.anagha.assignment

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.anagha.assignment.adapter.ProductsAdapter
import com.anagha.assignment.models.Row
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.NetworkError
import com.android.volley.NoConnectionError
import com.android.volley.ParseError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.ServerError
import com.android.volley.TimeoutError
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.Arrays
import java.util.HashMap

class ProductsFragment : Fragment() {
    private var readReviewsView: View? = null
    private var sreadReviewsRCV: SwipeRefreshLayout? = null
    private var readReviewsRCV: RecyclerView? = null
    private var productsadapter: ProductsAdapter? = null
    private var emptyTV: TextView? = null
    private var progressDialog: ProgressDialog? = null

    override fun onAttach(activity: Context?) {
        super.onAttach(activity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        readReviewsView = inflater.inflate(R.layout.productslist, container, false)
        onInitUi()
        onUIListener();
        return readReviewsView
    }

    private fun onUIListener() {

        sreadReviewsRCV?.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            if (NetworkUtil.hasNetwork(context!!)) {
                getProductReviews()
                sreadReviewsRCV!!.isRefreshing=false
            } else {
                Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun onInitUi() {
        readReviewsRCV = readReviewsView!!.findViewById(R.id.products_recyclerview)
        sreadReviewsRCV = readReviewsView!!.findViewById(R.id.products_Srecyclerview)
        emptyTV = readReviewsView!!.findViewById(R.id.emptyTV)
        //mToolbar = (Toolbar).findViewById(R.id.toolbar);
        readReviewsRCV!!.setHasFixedSize(true)
        /*linearlayout manager for list data,grid  for grid and stagged grid for stagged grid view*/
        val mLayoutManager = LinearLayoutManager(context)
        readReviewsRCV!!.layoutManager = mLayoutManager
        /*divider for recyclerview*/
        readReviewsRCV!!.addItemDecoration(DividerItemDecoration(activity!!, LinearLayoutManager.VERTICAL))
        /*Internet checking*/
        if (NetworkUtil.hasNetwork(context!!)) {
            getProductReviews()
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getProductReviews() {
        progressDialog = ProgressDialog(context)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setMessage("Please wait...")
        progressDialog!!.show()
        /*methode with GET Type for api calling*/
        val stringRequest = object : StringRequest(Request.Method.GET,
                resources.getString(R.string.basic_url) + resources.getString(R.string.end_url),
                Response.Listener { response ->
                    progressDialog!!.dismiss()
                    try {
                        val loginResponseObj = JSONObject(response)


                        //mToolbar.setTitle(Html.fromHtml("hii"));


                        if (loginResponseObj.has("rows")) {
                            //reviewslist.clear();
                            val jsonArray = loginResponseObj.get("rows") as JSONArray
                            if (jsonArray.length() != 0) {
                                val productReviewsListForAdapter = Arrays.asList(*Gson().fromJson(jsonArray.toString(), Array<Row>::class.java))
                                productsadapter = ProductsAdapter(productReviewsListForAdapter)
                                readReviewsRCV!!.adapter = productsadapter
                                productsadapter!!.notifyDataSetChanged()
                            } else {
                                emptyTV!!.text = resources.getString(R.string.no_records_found)
                                readReviewsRCV!!.visibility = View.GONE
                                emptyTV!!.visibility = View.VISIBLE
                            }
                        }
                    } catch (jse: JSONException) {
                        Log.e("JSONException", jse.message)
                        Toast.makeText(context, "JSONException", Toast.LENGTH_LONG).show()
                    }
                },
                Response.ErrorListener { error ->
                    progressDialog!!.dismiss()
                    Log.e("fail errorresponse", error.toString())
                    Toast.makeText(context, "fail errorresponse", Toast.LENGTH_LONG).show()
                    if (error is TimeoutError || error is NoConnectionError) {
                        Toast.makeText(context, "time out",
                                Toast.LENGTH_LONG).show()
                    } else if (error is AuthFailureError) {
                        //TODO
                        Toast.makeText(context, "AuthFailureError", Toast.LENGTH_LONG).show()

                    } else if (error is ServerError) {
                        //TODO
                        Toast.makeText(context, "ServerError", Toast.LENGTH_LONG).show()

                    } else if (error is NetworkError) {
                        //TODO
                        Toast.makeText(context, "NetworkError", Toast.LENGTH_LONG).show()

                    } else if (error is ParseError) {
                        //TODO
                        Toast.makeText(context, "ParseError", Toast.LENGTH_LONG).show()
                    }
                }) {
            override fun getHeaders(): Map<String, String> {
                return HashMap()
            }

            override fun getParams(): Map<String, String> {
                return HashMap()
            }
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(context!!)
        requestQueue.add(stringRequest)


    }

    override fun onDestroyView() {
        if (view != null) {
            val parent = view!!.parent as ViewGroup
            parent.removeAllViews()
        }
        super.onDestroyView()
    }
}
