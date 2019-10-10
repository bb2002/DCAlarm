package kr.saintdev.dcalarm.views.alert

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context

/**
 * @Date 09.23 2019
 * Alert Manager
 */
fun String.openAlert(context: Context, title: String = "메세지") {
    val alertDialog = AlertDialog.Builder(context)
    alertDialog.setTitle(title)
    alertDialog.setMessage(this)
    alertDialog.setPositiveButton("OK") { dialogInterface, _ ->
        dialogInterface.dismiss()
    }
    alertDialog.show()
}

fun Int.openAlert(context: Context, title: String = "메세지") {
    val msg = context.resources.getString(this)
    msg.openAlert(context, title)
}

fun String.openProgress(context: Context) : ProgressDialog {
    val dialog = ProgressDialog(context)
    dialog.setMessage(this)
    dialog.setCancelable(false)
    dialog.show()

    return dialog
}

fun Int.openProgress(context: Context) : ProgressDialog{
    val msg = context.resources.getString(this)
    return msg.openProgress(context)
}

interface OnAlertConfirmClickListener {
    fun onPositive()
    fun onNegative()
}

fun String.openConfirm(context: Context, title: String = "메세지", positiveText: String, negativeText: String, listener: OnAlertConfirmClickListener) {
    val alertDialog = AlertDialog.Builder(context)
    alertDialog.setPositiveButton(positiveText) {
        dialogInterface, _ -> dialogInterface.dismiss()
        listener.onPositive()
    }
    alertDialog.setNegativeButton(negativeText) {
        dialogInterface, _ -> dialogInterface.dismiss()
        listener.onNegative()
    }
    alertDialog.setTitle(title)
    alertDialog.setMessage(this)
    alertDialog.show()
}

fun Int.openConfirm(context: Context, title: String = "메세지", positiveText: Int, negativeText: Int, listener: OnAlertConfirmClickListener) {
    val msg = context.resources.getString(this)
    msg.openConfirm(context,
        title,
        context.resources.getString(positiveText),
        context.resources.getString(negativeText),
        listener)
}