import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.sportsnote.R

/**
 * メーラーを起動
 *
 * @param context コンテキスト
 * @param email 宛先メールアドレス
 * @param subject 件名
 * @param body 本文
 */
@SuppressLint("QueryPermissionsNeeded")
fun launchMailer(
    context: Context,
    email: String,
    subject: String,
    body: String
) {
    val mailtoUri = Uri.Builder()
        .scheme("mailto")
        .appendPath(email)
        .appendQueryParameter("subject", subject)
        .appendQueryParameter("body", body)
        .build()

    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = mailtoUri
    }

    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        // メールアプリが見つからなかった場合のエラーハンドリング
        Toast.makeText(context, context.getText(R.string.mailerNotFound), Toast.LENGTH_SHORT).show()
    }
}