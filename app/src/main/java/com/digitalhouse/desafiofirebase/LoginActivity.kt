package com.digitalhouse.desafiofirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tv_register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        btn_login.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)

            realizarLogin()

            startActivity(intent)
            finish()
        }
    }

    private fun realizarLogin() {
        val usuario = getUsuario()
        if (usuario != null) {
            sendFirebaseLogin(usuario)
        } else {
            sendMsg("Preencha o campo email e senha corretamente!")
        }
    }

    private fun sendMsg(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT)
            .show()
    }

    private fun sendFirebaseLogin(usuario: User) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(usuario.email, usuario.password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val firebaseUser = it.result?.user!!
                    val userFire = User(firebaseUser.email.toString(), "", firebaseUser.uid)
                    sendMsg("Login realizado com sucesso!")
                }
            }
    }

    private fun getUsuario(): User? {
        var email = etEmailLogin.text.toString()
        var senha = etPasswordLogin.text.toString()
        return if (!email.isNullOrBlank() and !senha.isNullOrBlank())
            User("", email, senha)
        else
            null
    }
}