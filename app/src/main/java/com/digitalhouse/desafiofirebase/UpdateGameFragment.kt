package com.digitalhouse.desafiofirebase

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.digitalhouse.desafiofirebase.databinding.FragmentDetailBinding
import com.digitalhouse.desafiofirebase.databinding.FragmentRegisterGameBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_register_game.*

class UpdateGameFragment : Fragment() {
    private val CODE_IMG: Int = 100
    private lateinit var storageReference: StorageReference
    private var game: Game = Game()

    lateinit var binding: FragmentRegisterGameBinding
    private val args: UpdateGameFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnImg.setOnClickListener {
            carregarImagem()
        }

        btnSave.setOnClickListener {
            game.name = etGameName.text.toString()
            game.year = etGameDate.text.toString()
            game.description = etDescription.text.toString()

            if (game.img == "") {
                Toast.makeText(context, "Adicione uma imagem antes de salvar!", Toast.LENGTH_SHORT).show()
            } else {
                salvarDados()
            }
        }

        Picasso.get().load(args.updateGame.img).fit().centerCrop().into(binding.ivCapa)

        binding.etGameName.setText(args.updateGame.name)
        binding.etGameDate.setText(args.updateGame.year)
        binding.etDescription.setText(args.updateGame.description)

        tbRegisterGame.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CODE_IMG) {

            val uploadFile = storageReference.putFile(data!!.data!!)
            uploadFile.continueWithTask { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Imagem Carrregada com sucesso!", Toast.LENGTH_SHORT)
                        .show()
                }
                storageReference!!.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    val url = downloadUri!!.toString()
                        .substring(0, downloadUri.toString().indexOf("&token"))

                    Log.i("URL da Imagem", url)
                    game.img = url

                    Picasso.get().load(url).into(ivCapa)
                }
            }.addOnFailureListener { task ->
                Log.i(TAG, "onActivityResult: $task")
            }
        }
    }

    fun carregarImagem() {
        storageReference = FirebaseStorage.getInstance().getReference(getUniqueKey())
        val intent = Intent()
        intent.type = "image/"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Get Image"), CODE_IMG)
    }

    private fun getUniqueKey() = FirebaseFirestore.getInstance().collection("pegando chave").document().id

    fun salvarDados() {
        val bancoDados = FirebaseFirestore.getInstance().collection("InfoGame")
        val id = getUniqueKey()
        game.id = id
        bancoDados.document(id).set(game)
    }

    fun updateDados(gameUpdate: Game) {
        val bancoDados = FirebaseFirestore.getInstance().collection("InfoGame")
        bancoDados.document(gameUpdate.id).set(game)
    }
}