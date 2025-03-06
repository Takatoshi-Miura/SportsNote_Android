import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.it6210.sportsnote.R
import com.it6210.sportsnote.utils.AppInfo

/**
 * お問い合わせメーラー起動
 *
 * @param context Context
 */
fun openInquiryMailer(context: Context) {
    val email = "SportsNote開発者<it6210ge@gmail.com>"
    val subject = context.getText(R.string.inquiry).toString()
    val deviceName = AppInfo.getDeviceName()
    val androidOSVer = AppInfo.getAndroidVersionInfo()
    val appVer = AppInfo.getAppVersion(context)
    val body =
        """
        お問い合わせ内容をご記入下さい。
                                
                                
        以下は削除しないでください。
        ■ご利用端末：$deviceName
        ■OSバージョン:$androidOSVer
        ■アプリバージョン:$appVer
        """.trimIndent()
    launchMailer(
        context = context,
        email = email,
        subject = subject,
        body = body,
    )
}

/**
 * メーラーを起動
 *
 * @param context コンテキスト
 * @param email 宛先メールアドレス
 * @param subject 件名
 * @param body 本文
 */
@SuppressLint("QueryPermissionsNeeded")
private fun launchMailer(
    context: Context,
    email: String,
    subject: String,
    body: String,
) {
    val mailtoUri =
        Uri.Builder()
            .scheme("mailto")
            .appendPath(email)
            .appendQueryParameter("subject", subject)
            .appendQueryParameter("body", body)
            .build()

    val intent =
        Intent(Intent.ACTION_SENDTO).apply {
            data = mailtoUri
        }

    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        // メールアプリが見つからなかった場合のエラーハンドリング
        Toast.makeText(context, context.getText(R.string.mailerNotFound), Toast.LENGTH_SHORT).show()
    }
}
