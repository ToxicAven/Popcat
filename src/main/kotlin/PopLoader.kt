import com.lambda.client.plugin.api.Plugin

internal object PopLoader: Plugin() {

    override fun onLoad() {
        hudElements.add(Popcat)
    }

    override fun onUnload() {
        // Here you can unregister threads etc...
    }
}