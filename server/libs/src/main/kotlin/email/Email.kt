package email

import org.apache.velocity.app.VelocityEngine
import org.apache.velocity.runtime.RuntimeConstants
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.ui.velocity.VelocityEngineUtils
import javax.mail.MessagingException
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

object Email {
    var primaryEmail_user = ""
    var primaryEmail_pass = ""
    var from = ""
    var server = ""
    var port = "587"

    fun sendHTMLEmail(subject: String, to: String, template: String, hTemplateVariables: MutableMap<String, Any>) {
        try {
            // Step 1
            val mailServerProperties = System.getProperties()

            mailServerProperties.put("mail.smtp.from", from)
            mailServerProperties.put("mail.smtp.user", primaryEmail_user)
            mailServerProperties.put("mail.smtp.password", primaryEmail_pass)
            mailServerProperties.put("mail.smtp.host", server)
            mailServerProperties.put("mail.smtp.port", port)
            mailServerProperties.put("mail.smtp.auth", "true")
            mailServerProperties.put("mail.smtp.starttls.enable", "true")

            val getMailSession = Session.getDefaultInstance(mailServerProperties, null)

            val message = MimeMessage(getMailSession)
            message.subject = subject

            val helper: MimeMessageHelper
            helper = MimeMessageHelper(message, true)
            helper.setFrom(InternetAddress(from))
            helper.setTo(InternetAddress(to))

            val velocityEngine = VelocityEngine()
            velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath")
            velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader::class.java.name)

            velocityEngine.init()

            val text = VelocityEngineUtils.mergeTemplateIntoString(
                    velocityEngine,
                    template,
                    hTemplateVariables)

            helper.setText(text, true)


            val transport = getMailSession.getTransport("smtp")

            transport.connect(
                    Email.server,
                    Email.primaryEmail_user,
                    Email.primaryEmail_pass
            )

            transport.sendMessage(message, message.allRecipients)
            transport.close()


        } catch (ex: MessagingException) {
            ex.printStackTrace()
        }

    }
}
