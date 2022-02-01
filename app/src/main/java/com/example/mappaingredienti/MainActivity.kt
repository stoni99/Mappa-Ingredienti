package com.example.mappaingredienti

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class MainActivity : AppCompatActivity(), AdaptRicetta.ItemClickListener {
    private var recyclerView: RecyclerView? = null
    private var mAdapter: AdaptRicetta? = null
    private var recipeList: ArrayList<Ricetta>? = null
    private var db: FirebaseFirestore? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recipeList = ArrayList<Ricetta>()
        recyclerView = findViewById(R.id.recycler_ricette)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.setLayoutManager(linearLayoutManager)
        mAdapter = AdaptRicetta()r(this, recipeList)
        mAdapter.setClickListener(this
        recyclerView.setAdapter(mAdapter)
        val dividerItemDecoration = DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)
        db = FirebaseFirestore.getInstance()
        val querySnapshotTask: Task<QuerySnapshot> = db.collection("Recipe").get()
                .addOnSuccessListener(OnSuccessListener<Any> { queryDocumentSnapshots ->
                    if (!queryDocumentSnapshots.isEmpty()) {
                        val list: List<DocumentSnapshot> = queryDocumentSnapshots.getDocuments()
                        Log.e("Lista", "list: $list")
                        for (d in list) {
                            val p: Ricetta = d.toObject(Ricetta::class)
                            recipeList!!.add(p)
                        }
                        mAdapter.notifyDataSetChanged()
                    }
                }
                )
        Log.e("Lista", "Recipelist: $recipeList")
    }

    fun onItemClick(view: View?, position: Int) {
        val recipe: Ricetta = recipeList!![position]
        Log.e("Ricetta", "Ingredienti: " + recipe.getIngredients())
        //  Log.v("RECIPE",recipe.getName()); visualizza nome di recipe nei log
        val i = Intent(this, RecipeViewerActivity::class)
        i.putExtra("RECIPE", recipe)
        startActivity(i)
    } // ...
}