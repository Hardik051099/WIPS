import android.app.Activity
import android.graphics.Color
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import com.example.wips.R

fun Toast.showCustomToast(message: String,State:Boolean, activity: Activity)
{
    val layout = activity.layoutInflater.inflate (
        R.layout.custom_toast_layout,
        activity.findViewById(R.id.toast_container)
    )

    // set the text of the TextView of the message
    val textView = layout.findViewById<TextView>(R.id.toast_text)
    val border = layout.findViewById<FrameLayout>(R.id.button_accent_border)
    if (State){
        border.setBackgroundColor(Color.parseColor("#FFBB86FC"))
    }
    else{
        border.setBackgroundColor(Color.RED)
    }
   val color = textView.resources.getColor(R.color.purple_500)
    textView.setTextColor(color)
    textView.text = message

    // use the application extension function
    this.apply {
        setGravity(Gravity.BOTTOM, 0, 40)
        duration = Toast.LENGTH_LONG
        view = layout
        show()
    }
}


