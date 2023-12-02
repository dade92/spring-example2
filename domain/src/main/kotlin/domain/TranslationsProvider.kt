package domain

interface TranslationsProvider {
    fun retrieve(language: String): Translations
}

data class Translations(
    val data: Map<String, String>
)

class StaticTranslationsProvider : TranslationsProvider {

    override fun retrieve(language: String): Translations =
        Translations(
            mapOf(
                "appflow.customerData.hi" to "Hi",
                "appflow.needHelp.called" to "Need help called!",
                "appflow.needHelp.title" to "Need help?",
                "appflow.customerData.next" to "Next",
                "appflow.customerData.warning" to "Warning",
                "appflow.customerData.t_and_c" to "Accept t&c",
                "appflow.customerData.alias" to "Your alias",
                "appflow.customerData.actions" to "Actions",
                "appflow.customerData.submit" to "Submit",
                "appflow.customerData.undo" to "Undo",
                "appflow.customerData.areyousure" to "Are you sure???",
                "appflow.customerData.confirmModal" to "By clicking on confirm you confirm the operation",
                "appflow.customerData.confirm" to "Confirm",
                "appflow.customerData.disagree" to "Disagree",
                "appflow.customerData.thankyoumessage" to "Thanks for your selection ",
                "appflow.customerData.restart" to "Restart",
                "appflow.customerData.photo" to "Upload your picture",
                "appflow.customerData.alertmessage" to "Input must be greater than two letters",
                "appflow.customerData.noUser" to "Error while loading user information",
                "appflow.loading.loading_message" to "Please wait while we complete the requested operation"
            )
        )

}