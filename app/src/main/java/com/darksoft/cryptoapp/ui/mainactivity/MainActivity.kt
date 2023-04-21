package com.darksoft.cryptoapp.ui.mainactivity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.darksoft.cryptoapp.R
import com.darksoft.cryptoapp.databinding.ActivityMainBinding
import com.darksoft.cryptoapp.ui.mainactivity.model.CriptoModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private lateinit var list: ArrayList<CriptoModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup RecyclerView
        list = ArrayList()
        apiData
        recyclerViewAdapter = RecyclerViewAdapter(this, list)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = recyclerViewAdapter

    }

    val apiData: Unit
    get() {
        val url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest"

        val queue = Volley.newRequestQueue(this)
        val jsonObjectRequest: JsonObjectRequest =
            @SuppressLint("NotifyDataSetChanged")
            object: JsonObjectRequest(Method.GET, url, null, { response ->
            try {
                val data = response.getJSONArray("data")
                for (i in 0 until data.length()){
                    val dataObject = data.getJSONObject(i)
                    val name = dataObject.getString("name")
                    val symbol = dataObject.getString("symbol")
                    val quote = dataObject.getJSONObject("quote")
                    val price =  quote.getJSONObject("USD").getDouble("price")

                    val model = CriptoModel(name, "$ $price", symbol)
                    list.add(model)
                }
                recyclerViewAdapter.notifyDataSetChanged()
                println(list)
            }catch (_: Exception){
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
            }
        }, { error ->
            Toast.makeText(this, error.message.toString(), Toast.LENGTH_SHORT).show()
        }) {
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["X-CMC_PRO_API_KEY"] = "2d2a8cce-a8bb-4c1c-84a1-46aec379c902"
                    return headers
                }
        }

        queue.add(jsonObjectRequest)
    }

}