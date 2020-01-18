package com.anagha.assignment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anagha.assignment.adapter.ProductsAdapter;
import com.anagha.assignment.models.Row;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductsFragment extends Fragment {
    private View readReviewsView;
    private RecyclerView readReviewsRCV;
    private ProductsAdapter productsadapter;
    private TextView emptyTV;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        readReviewsView = inflater.inflate(R.layout.productslist, container, false);
        onInitUi();
        return readReviewsView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void onInitUi() {
        readReviewsRCV = readReviewsView.findViewById(R.id.products_recyclerview);
        emptyTV = readReviewsView.findViewById(R.id.emptyTV);
        readReviewsRCV.setHasFixedSize(true);
        /*linearlayout manager for list data,grid  for grid and stagged grid for stagged grid view*/
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        readReviewsRCV.setLayoutManager(mLayoutManager);
        /*divider for recyclerview*/
        readReviewsRCV.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        /*Internet checking*/
        if (NetworkUtil.hasNetwork(getContext())) {
            getProductReviews();
        } else {
            Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private ProgressDialog progressDialog;

    private void getProductReviews() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        /*methode with GET Type for api calling*/
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                getResources().getString(R.string.basic_url) +
                        getResources().getString(R.string.end_url),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject loginResponseObj = new JSONObject(response);
                            if (loginResponseObj.has("rows")) {
                                //reviewslist.clear();
                                JSONArray jsonArray = (JSONArray) loginResponseObj.get("rows");
                                if (jsonArray.length() != 0) {
                                    List<Row> productReviewsListForAdapter = Arrays.asList(new Gson().fromJson(jsonArray.toString(), Row[].class));
                                    productsadapter = new ProductsAdapter(productReviewsListForAdapter);
                                    readReviewsRCV.setAdapter(productsadapter);
                                    productsadapter.notifyDataSetChanged();
                                } else {
                                    emptyTV.setText(getResources().getString(R.string.no_records_found));
                                    readReviewsRCV.setVisibility(View.GONE);
                                    emptyTV.setVisibility(View.VISIBLE);
                                }
                            }
                        } catch (JSONException jse) {
                            Log.e("JSONException", jse.getMessage());
                            Toast.makeText(getContext(), "JSONException", Toast.LENGTH_LONG).show();
                        }
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Log.e("fail errorresponse", error.toString());
                        Toast.makeText(getContext(), "fail errorresponse", Toast.LENGTH_LONG).show();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(getContext(), "time out",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            Toast.makeText(getContext(), "AuthFailureError", Toast.LENGTH_LONG).show();

                        } else if (error instanceof ServerError) {
                            //TODO
                            Toast.makeText(getContext(), "ServerError", Toast.LENGTH_LONG).show();

                        } else if (error instanceof NetworkError) {
                            //TODO
                            Toast.makeText(getContext(), "NetworkError", Toast.LENGTH_LONG).show();

                        } else if (error instanceof ParseError) {
                            //TODO
                            Toast.makeText(getContext(), "ParseError", Toast.LENGTH_LONG).show();
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                return new HashMap<>();
            }

            @Override
            protected Map<String, String> getParams() {
                return new HashMap<>();
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);


    }

    @Override
    public void onDestroyView() {
        if (getView() != null) {
            ViewGroup parent = (ViewGroup) getView().getParent();
            parent.removeAllViews();
        }
        super.onDestroyView();
    }
}
