package com.example.kleberstevendiazcoello.ui.fragments;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.kleberstevendiazcoello.ui.Database.Database;
import com.example.kleberstevendiazcoello.ui.R;
import com.example.kleberstevendiazcoello.ui.clases_utilitarias.Platos;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fragment_DetalleHistorial.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fragment_DetalleHistorial#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_DetalleHistorial extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView txtfecha;
    TextView txttotalcarbs,txtinsulinatot,txthora,txtglucosaact,txtglucosaobj;
    ImageView back_histo;
    EditText editpost;
    Button ingresopost,btn_guardar,btn_cancelar;
    Dialog poppost;
    RequestQueue requestQueue;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public fragment_DetalleHistorial() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_DetalleHistorial.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_DetalleHistorial newInstance(String param1, String param2) {
        fragment_DetalleHistorial fragment = new fragment_DetalleHistorial();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_fragment__detalle_historial, container, false);
        String carbs =getArguments().getString("TotalCarb");
        String fecha = getArguments().getString("Fecha");
        String Insulina = getArguments().getString("Insulina");
        String hora = getArguments().getString("Hora");
        String ga = getArguments().getString("glucosaa");
        String go = getArguments().getString("glucosao");
        final int  idh = getArguments().getInt("id_historial");
        back_histo = (ImageView)view.findViewById(R.id.back_historial);
        txthora =(TextView)view.findViewById(R.id.historialhora);
        txtfecha = (TextView)view.findViewById(R.id.historialfecha);
        txttotalcarbs = (TextView)view.findViewById(R.id.dhistorialtotal);
        txtinsulinatot =(TextView)view.findViewById(R.id.historialInsulina);
        txtglucosaact = (TextView)view.findViewById(R.id.glucosaactualshow);
        txtglucosaobj = (TextView)view.findViewById(R.id.glucosaobjetivoshow);
        ingresopost = (Button)view.findViewById(R.id.ingresopost);
        requestQueue = Volley.newRequestQueue(getActivity());
        txtfecha.setText(fecha);
        txtinsulinatot.setText(Insulina);
        txttotalcarbs.setText(carbs);
        txthora.setText(hora);
        txtglucosaact.setText(ga);
        txtglucosaobj.setText(go);
        back_histo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(getActivity()).cleanListAuto();
                android.support.v4.app.FragmentManager fragmentManager= getFragmentManager();
                android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content2,new HistorialFragment()).commit();
            }
        });


        ingresopost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                poppost = new Dialog(getActivity());
                poppost.setContentView(R.layout.popup_post);
                btn_guardar = (Button)poppost.findViewById(R.id.btnguardarpost);
                btn_cancelar = (Button)poppost.findViewById(R.id.btncancelarpost);
                editpost = (EditText)poppost.findViewById(R.id.txtpost);
                btn_guardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String postpan = editpost.getText().toString();
                        String idhisto = String.valueOf(idh);
                        ingresarpostpandrial(postpan,idhisto);
                        poppost.dismiss();
                    }

                });
                btn_cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        poppost.dismiss();
                    }
                });
                poppost.show();
            }
        });


        return view;
    }


    void ingresarpostpandrial(final String postpand,final String idhis){
        String url = "http://www.flexoviteq.com.ec/InsuvidaFolder/GlucosaPostMod.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Modificado Sastifactoriamente", Snackbar.LENGTH_LONG).setAction("Action", null).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put("glucosapost",postpand);
                map.put("idh",idhis);
                return map;
            }

        };
        requestQueue.add(request);
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            Toast.makeText(context,"Notificacion Detalle Historial de Fragmento",Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
