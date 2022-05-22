package cf.untitled.salend.model

data class KategoriStoreData(
    val sId : String,
    val sName : String,
    val sLocation : String,
    val sTime : String,
    val sImage : String
)

class KategoriStore : ArrayList<KategoriStoreData>()
