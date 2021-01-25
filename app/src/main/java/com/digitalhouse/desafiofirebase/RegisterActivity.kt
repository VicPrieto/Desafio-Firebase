package com.digitalhouse.desafiofirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btn_register.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)

            registerUser()

            startActivity(intent)
            finish()
        }
    }

    private fun registerUser() {
        val user = getUser()
        if (user != null) {
            sendFirebaseCad(user)
        }
        else {
            sendMsg("Preencha o campo email e senha corretamente!")
        }

    }

    private fun sendFirebaseCad(user: User) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(user.email,user.password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    val firebaseUser = it.result?.user!!
                    val userFire = User(firebaseUser.email.toString(),"",firebaseUser.uid)
                    sendMsg("Usuario cadastrado!")
                }

            }.addOnFailureListener {
                sendMsg("$it, deu erro :c")
            }
    }

    private fun sendMsg(msg : String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT)
            .show()
    }

    private fun getUser(): User? {
        var nome = etName.text.toString()
        var email = etEmail.text.toString()
        var senha = etPassword.text.toString()
        return if (!email.isNullOrEmpty() and !senha.isNullOrEmpty())
            User(nome, email, senha)
        else
            null
    }
}