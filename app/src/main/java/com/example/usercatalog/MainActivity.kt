package com.example.usercatalog

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextAge: EditText
    private lateinit var listViewUsers: ListView
    private lateinit var users: MutableList<User>
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var buttonSave: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Каталог пользователей"

        editTextName = findViewById(R.id.editTextName)
        editTextAge = findViewById(R.id.editTextAge)
        listViewUsers = findViewById(R.id.listViewUsers)
        buttonSave = findViewById(R.id.buttonSave)

        users = mutableListOf()

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        listViewUsers.adapter = adapter

        buttonSave.setOnClickListener {
            saveUser()
        }

        listViewUsers.setOnItemClickListener { _, _, position, _ ->
            removeUser(position)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_exit -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveUser() {
        val name = editTextName.text.toString()
        val ageText = editTextAge.text.toString()

        if (name.isNotBlank() && ageText.isNotBlank()) {
            val age = ageText.toInt()
            val user = User(name, age)
            users.add(user)
            adapter.add("${user.name}, ${user.age} лет")
            editTextName.text.clear()
            editTextAge.text.clear()
        } else {
            Toast.makeText(this, "Заполните оба поля", Toast.LENGTH_SHORT).show()
        }
    }

    private fun removeUser(position: Int) {
        users.removeAt(position)
        adapter.remove(adapter.getItem(position))
        adapter.notifyDataSetChanged()
    }
}