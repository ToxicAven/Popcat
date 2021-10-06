import com.lambda.client.plugin.api.PluginHudElement
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.ResourceLocation
import com.lambda.client.util.TickTimer
import com.lambda.client.util.TimeUnit
import com.lambda.client.util.graphics.GlStateUtils
import com.lambda.client.util.graphics.VertexHelper
import com.lambda.client.util.math.Vec2d
import com.lambda.client.util.threads.runSafe
import com.lambda.commons.utils.MathUtils
import net.minecraft.client.renderer.texture.DynamicTexture
import org.lwjgl.opengl.GL11.*
import java.net.URL
import javax.imageio.ImageIO

internal object Popcat: PluginHudElement(
    name = "Popcat",
    category = Category.MISC,
    description = "Ez pop!",
    pluginMain = PopLoader
) {
    override val hudWidth: Float = 122.0f
    override val hudHeight: Float = 122.0f
    private const val popScale = 1.0f
    private var popcount = 0
    private val alternate = TickTimer(TimeUnit.MILLISECONDS)
    private val popOne : ResourceLocation = mc.textureManager.getDynamicTextureLocation("toxic/aven", DynamicTexture(ImageIO.read(URL("https://i.toxicaven.dev/RSgGYQtWOP5q/direct.png"))))
    private val popTwo : ResourceLocation = mc.textureManager.getDynamicTextureLocation("toxic/aven", DynamicTexture(ImageIO.read(URL("https://i.toxicaven.dev/LbMgZ9K6t6BK/direct.png"))))

    private val popSpeed by setting("Speed", 100, 75..300, 5)

    override fun renderHud(vertexHelper: VertexHelper) {
        super.renderHud(vertexHelper)
        runSafe {
            popTimer()
        }
    }

    private fun popTimer() {
        if (popcount >= 100000) popcount = 0
        if (alternate.tick(popSpeed)) popcount += 1
        if (MathUtils.isNumberEven(popcount)) drawPop(popOne)
        else drawPop(popTwo)
    }

    private fun drawPop(location: ResourceLocation) {
        val tessellator = Tessellator.getInstance()
        val buffer = tessellator.buffer
        GlStateUtils.texture2d(true)

        mc.renderEngine.bindTexture(location)
        GlStateManager.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)

        val center = Vec2d(61.0, 61.0)
        val halfWidth = popScale * 61.0
        val halfHeight = popScale * 61.0
        buffer.begin(GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_TEX)
        buffer.pos(center.x - halfWidth, center.y - halfHeight, 0.0).tex(0.0, 0.0).endVertex()
        buffer.pos(center.x - halfWidth, center.y + halfHeight, 0.0).tex(0.0, 1.0).endVertex()
        buffer.pos(center.x + halfWidth, center.y - halfHeight, 0.0).tex(1.0, 0.0).endVertex()
        buffer.pos(center.x + halfWidth, center.y + halfHeight, 0.0).tex(1.0, 1.0).endVertex()
        tessellator.draw()

        GlStateManager.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
    }
}