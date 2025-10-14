import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.projetopaz.frontend_paz.App
import kotlinx.browser.document
import org.w3c.dom.HTMLBodyElement

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport(document.body as HTMLBodyElement) {
        App()
    }
}