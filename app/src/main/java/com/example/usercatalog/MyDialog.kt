package com.example.usercatalog

import android.app.AlertDialog
import android.content.Context

class MyDialog(private val context: Context) {

    fun showConfirmationDialog(onConfirm: () -> Unit) {
        val builder = AlertDialog.Builder(context)
            //builder можно опустить
            .setCancelable(false)
            .setTitle("Удаление пользователя")
            .setMessage("Вы уверены, что хотите удалить этого пользователя?")

            .setPositiveButton("Да") { dialog, _ ->
                onConfirm()
                dialog.cancel()
            }

            .setNegativeButton("Нет") { dialog, _ ->
                dialog.cancel()
            }

            .create()

        builder.show()
    }
}
