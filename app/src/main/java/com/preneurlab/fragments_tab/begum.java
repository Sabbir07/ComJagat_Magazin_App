package com.preneurlab.fragments_tab;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.preneurlab.comjagatmagazin.DownloadActivity;
import com.preneurlab.comjagatmagazin.R;
import com.preneurlab.comjagatmagazin.ReadActivity;
import com.preneurlab.internet.HttpHandler;
import com.preneurlab.internet.MobileInternetConnectionDetector;
import com.preneurlab.internet.URLSettings;
import com.preneurlab.internet.WIFIInternetConnectionDetector;
import com.preneurlab.offline_data.MyJSON2016;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class begum extends Fragment {
    private ListView feedListView = null;
    private String pdfLink;

    private ArrayList<HashMap<String, Object>> hashMapArrayList;
    private Bitmap bitmap;
    private SimpleAdapter simpleAdapter;

    private MobileInternetConnectionDetector cd;
    private WIFIInternetConnectionDetector wcd;
    private Boolean isConnectionExist = false;
    private Boolean isConnectionExistWifi = false;

    private String json;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab, container, false);

        feedListView = (ListView) rootView.findViewById(R.id.custom_list);
        hashMapArrayList = new ArrayList<>();

        checkInternetConnection();


        return rootView;
    }

    private void checkInternetConnection() {
        cd = new MobileInternetConnectionDetector(getActivity());
        wcd = new WIFIInternetConnectionDetector(getActivity());
        isConnectionExist = cd.checkMobileInternetConn();
        isConnectionExistWifi = wcd.checkMobileInternetConn();

        if (isConnectionExistWifi || isConnectionExist) {
            new DownloadFilesTask().execute();
//            Toast.makeText(getActivity(), "Yes", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "No Internet Connection!", Toast.LENGTH_LONG).show();
            json = MyJSON2016.getData(getActivity());
            new DownloadFilesTaskOffLine().execute();
        }
    }


    //AsyncTask Class
    private class DownloadFilesTask extends AsyncTask<String, Integer, Void> {

        @Override
        protected void onProgressUpdate(Integer... values) {

        }


        @Override
        protected Void doInBackground(String... params) {

            HttpHandler httpHandler = new HttpHandler();
            String jsonString = httpHandler.makeServiceCall(URLSettings.begum);

            MyJSON2016.saveData(getActivity(), jsonString);

            if (jsonString != null) {
                try {
                    JSONObject rootJsonObject = new JSONObject(jsonString);
                    JSONArray jsonArray = rootJsonObject.optJSONArray("items");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String pdfLink = jsonObject.getString("id");

                        JSONObject snippet = jsonObject.getJSONObject("snippet");

                        String title = snippet.getString("title");

                        Log.d("id" + pdfLink, "title" + title);

                        JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                        JSONObject aDefault = thumbnails.getJSONObject("default");

                        String thumbnail = aDefault.getString("url");

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("month", title);
                        hashMap.put("pdf", pdfLink);
                        hashMap.put("thumb", thumbnail);

//                        // getting bitmap from image url
//                        try {
//                            URL url = new URL(thumbnail);
//                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                            connection.setDoInput(true);
//                            connection.connect();
//                            InputStream input = connection.getInputStream();
//                            bitmap = BitmapFactory.decodeStream(input);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        hashMap.put("image", bitmap);
                        hashMapArrayList.add(hashMap);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d("URL:", URLSettings.comjagat2016);
            if (null != hashMapArrayList) {
                //update listview with json data
                updateList();
            }
        }
    }

    //AsyncTask Class offline
    private class DownloadFilesTaskOffLine extends AsyncTask<String, Integer, Void> {

        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        @Override
        protected Void doInBackground(String... params) {

//            HttpHandler httpHandler = new HttpHandler();
//            String jsonString = httpHandler.makeServiceCall(URLSettings.comjagat2016);

            if (json != null) {
                try {
                    JSONObject rootJsonObject = new JSONObject(json);
                    JSONArray jsonArray = rootJsonObject.optJSONArray("posts");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String title = jsonObject.getString("title");
                        String pdfLink = jsonObject.getString("content");
                        String thumbnail = jsonObject.getString("thumbnail");

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("month", title);
                        hashMap.put("pdf", pdfLink);
                        hashMap.put("thumb", thumbnail);

//                        // getting bitmap from image url
//                        try {
//                            URL url = new URL(thumbnail);
//                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                            connection.setDoInput(true);
//                            connection.connect();
//                            InputStream input = connection.getInputStream();
//                            bitmap = BitmapFactory.decodeStream(input);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        hashMap.put("image", bitmap);
                        hashMapArrayList.add(hashMap);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d("URL:", URLSettings.comjagat2016);
            if (null != hashMapArrayList) {
                //update listview with json data
                updateList();
            }
        }
    }


    //method updateList()
    public void updateList() {
        feedListView.setVisibility(View.VISIBLE);

//        if no images contain into json data then SimpleAdapter this 2 lines just enough
//        simpleAdapter = new SimpleAdapter(getActivity(), hashMapArrayList, R.layout.custom_post_layout,
//                new String[]{"month", "image"}, new int[]{R.id.month, R.id.coverPhoto});
//        feedListView.setAdapter(simpleAdapter);

        try {
            simpleAdapter = new SimpleAdapter(getActivity(), hashMapArrayList, R.layout.custom_post_layout,
                    new String[]{"month", "image"}, new int[]{R.id.month, R.id.coverPhoto}) {

                @Override
                public View getView(final int pos, View convertView, ViewGroup parent) {
                    View v = convertView;
                    if (v == null) {
                        LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        v = vi.inflate(R.layout.custom_post_layout, null);
                    }

                    ImageView img = (ImageView) v.findViewById(R.id.coverPhoto);
//                    img.setImageBitmap((Bitmap) hashMapArrayList.get(pos).get("image"));

                    TextView tv = (TextView) v.findViewById(R.id.month);
                    tv.setText(hashMapArrayList.get(pos).get("month").toString());

                    Picasso.with(getActivity())
                            .load(hashMapArrayList.get(pos).get("thumb").toString())
                            .into(img);

                    final ViewHolder holder = new ViewHolder();

                    holder.tvOnlineRead = (TextView) v.findViewById(R.id.tvOnlineRead);
                    holder.tvOfflineRead = (TextView) v.findViewById(R.id.tvOfflineRead);

                    holder.tvOnlineRead.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            pdfLink = hashMapArrayList.get(pos).get("pdf").toString();
                            Animation animFadin = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                            holder.tvOnlineRead.startAnimation(animFadin);
                            Intent intent = new Intent(getActivity(), ReadActivity.class);
                            intent.putExtra("PDFLINK", pdfLink);
                            getActivity().startActivity(intent);
                        }
                    });

                    holder.tvOfflineRead.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            pdfLink = hashMapArrayList.get(pos).get("pdf").toString();
                            Animation animFadin = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                            holder.tvOfflineRead.startAnimation(animFadin);
                            Intent intent = new Intent(getActivity(), DownloadActivity.class);
                            intent.putExtra("PDFLINK", pdfLink);
                            getActivity().startActivity(intent);
                        }
                    });
                    return v;
                }
            };


//            //for bitmaping image
//            simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
//                @Override
//                public boolean setViewValue(View view, Object data, String textRepresentation) {
//                    if ((view instanceof ImageView) & (data instanceof Bitmap)) {
//                        ImageView iv = (ImageView) view;
//                        Bitmap bm = (Bitmap) data;
//                        iv.setImageBitmap(bm);
//                        return true;
//                    }
//                    return false;
//                }
//            });


            feedListView.setAdapter(simpleAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }


        //this operation and online read operations are same funtionality
        feedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Toast.makeText(getActivity(),"Yes",Toast.LENGTH_LONG).show();
                pdfLink = hashMapArrayList.get(position).get("pdf").toString();
                Intent intent = new Intent(getActivity(), ReadActivity.class);
                intent.putExtra("PDFLINK", pdfLink);
                getActivity().startActivity(intent);
            }
        });
    }

    static class ViewHolder {
        private TextView tvOnlineRead, tvOfflineRead;
    }
}
