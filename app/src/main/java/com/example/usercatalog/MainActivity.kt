package com.example.usercatalog

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextAge: EditText
    private lateinit var listViewUsers: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var buttonSave: Button

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        listViewUsers.adapter = adapter

        // Подписываемся на изменения списка пользователей
        userViewModel.users.observe(this) { users ->
            adapter.clear()
            adapter.addAll(users.map { "${it.name}, ${it.age} лет" })
        }

        buttonSave.setOnClickListener {
            saveUser(it)
        }

        listViewUsers.setOnItemClickListener { _, _, position, _ ->
            removeUser(position)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        // Поменяем цвет текста на черный
        for (i in 0 until (menu?.size() ?: 0)) {
            val menuItem = menu?.getItem(i)
            val spanString = SpannableString(menuItem?.title.toString())
            spanString.setSpan(
                ForegroundColorSpan(Color.BLACK),
                0,
                spanString.length,
                0
            )
            menuItem?.title = spanString
        }
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

    private fun saveUser(view: View) {
        val name = editTextName.text.toString()
        val ageText = editTextAge.text.toString()

        if (name.isNotBlank() && ageText.isNotBlank()) {
            val age = ageText.toIntOrNull()
            if (age != null) {
                val user = User(name, age)
                val users = userViewModel.users.value ?: mutableListOf()
                users.add(user)
                userViewModel.users.value = users
                editTextName.text.clear()
                editTextAge.text.clear()
            } else {
                Snackbar.make(view, "Введите корректный возраст", Snackbar.LENGTH_SHORT).show()
            }
        } else {
            Snackbar.make(view, "Заполните оба поля", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun removeUser(position: Int) {
        val users = userViewModel.users.value ?: return
        if (position >= 0 && position < users.size) {
            users.removeAt(position)
            userViewModel.users.value = users
        }
    }
}
