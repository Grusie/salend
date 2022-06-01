package cf.untitled.salend.model

data class UserData(
    var u_id: String? = "",
    var u_name: String? = "",
    var u_store_favorite: List<String?>?,
    var u_item_favorite: List<String?>?
)
