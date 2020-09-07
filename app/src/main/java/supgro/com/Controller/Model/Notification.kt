package supgro.com.Controller.Model

class Notification {

    private var userid: String = ""
    private var text: String = ""
    private var postid: String = ""
    private var ispost: Boolean = false

    constructor()
    constructor(userid: String, text: String, postid: String, ispost: Boolean) {
        this.userid = userid
        this.text = text
        this.postid = postid
        this.ispost = ispost
    }


    //___________________________________________

    fun getUserId(): String{
        return userid
    }
    fun setUserId(userid: String){
        this.userid = userid
    }

    //___________________________________________

    fun getText(): String{
        return text
    }
    fun setText(text: String){
        this.text = text
    }

    //___________________________________________

    fun getPostid(): String{
        return postid
    }
    fun setPostid(postid: String){
        this.postid = postid
    }

    //___________________________________________

    fun isIsPost(): Boolean{
        return ispost
    }
    fun setIsPost(ispost: Boolean){
        this.ispost = ispost
    }

}