package cf.untitled.salend.model

data class CategoryStoreData(
    val _id : String,
    val s_name : String,
    val s_time : String,
    val s_image : String
)

data class CategoryStore(
    val stores : List<CategoryStoreData>?
)
