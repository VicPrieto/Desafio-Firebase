package com.digitalhouse.desafiofirebase

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class HomeFragment : Fragment(), ItemAdapter.OnItemClickListener {

    lateinit var itemAdapter: ItemAdapter
    lateinit var gridLayoutManager: GridLayoutManager
    private var gameList = MutableLiveData<ArrayList<Game>>()
    val scope = CoroutineScope(Dispatchers.Default)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        getGames()
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gridLayoutManager = GridLayoutManager(context, 2)
        rvGames.layoutManager = gridLayoutManager
        rvGames.hasFixedSize()
//        itemAdapter.listaGames = gameList
        gameList.observe(viewLifecycleOwner, {
            itemAdapter = ItemAdapter(it, this)
            rvGames.adapter = itemAdapter
            Log.i("TESTE", "$it")
        })

        addNewGame.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_registerGameFragment)
        }
    }

    override fun onClick(position: Int) {
        val clickedItem = gameList.value?.get(position)
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment(clickedItem!!))
    }

    fun getGames() {
        val bancoDados = Firebase.firestore.collection("InfoGame")
        val listaGamesLocal = ArrayList<Game>()
        scope.launch {
            val listaGamesRemoto = bancoDados.get().await()
            listaGamesRemoto.forEach { doc ->
                listaGamesLocal.add(doc.toObject())
            }
            gameList.postValue(listaGamesLocal)
        }
    }

    override fun onResume() {
        super.onResume()
        getGames()
    }

}