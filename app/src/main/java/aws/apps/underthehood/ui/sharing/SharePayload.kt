package aws.apps.underthehood.ui.sharing

data class SharePayload(val fileName: String,
                        val subject: String,
                        val text: String)