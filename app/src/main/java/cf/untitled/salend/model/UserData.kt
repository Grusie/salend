package cf.untitled.salend.model

data class UserData(        //파이어베이스에 회원가입 시, 정보 수정 시 사용하기 위한 데이터 클래스
    var u_id: String? = "",
    var u_name: String? = "",
    var u_store_favorite: ArrayList<String?>,
    var u_item_favorite: ArrayList<String?>,
    var u_pay_list : ArrayList<String?>
)
