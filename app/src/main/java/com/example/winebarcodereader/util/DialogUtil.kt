package com.example.winebarcodereader.util

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.example.winebarcodereader.R

object DialogUtil {
    fun quitApplicationDialog(context: Context, okListener:DialogInterface.OnClickListener, cancelListener: DialogInterface.OnClickListener){
        var title:String = context.resources.getString(R.string.quit_app_dialog_title)
        var message:String = context.resources.getString(R.string.quit_app_dialog_message)

        showDialogWithTitleAndMessage(context, title, message, okListener, cancelListener)
    }

    /**
     * 메시지, 타이틀을 가진 기본 Dialog 띄우기
     */
    fun showDialogWithTitleAndMessage(context: Context,title:String, message:String, okListener:DialogInterface.OnClickListener, cancelListener: DialogInterface.OnClickListener){
        var builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setCancelable(false)

        var listener = DialogInterface.OnClickListener { p0, p1 ->
            when (p1) {
                DialogInterface.BUTTON_POSITIVE ->{
                    okListener.onClick(p0,p1)
                }
                DialogInterface.BUTTON_NEGATIVE ->{
                    cancelListener.onClick(p0,p1)
                }
            }
        }

        builder.setPositiveButton("확인", listener)
        builder.setNegativeButton("취소", listener)

        builder.show()
    }
}