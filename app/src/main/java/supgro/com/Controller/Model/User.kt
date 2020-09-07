package supgro.com.Controller.Model

class User {

    private var username: String = ""
    private var image: String = ""
    private var uid: String = ""
    private var fullname: String = ""
    private var about: String = ""


    constructor()

    constructor(username: String, image: String, uid: String, fullname: String){
        this.username = username
        this.image = image
        this.uid = uid
        this.fullname = fullname
        this.about = about
    }

    fun getUsername(): String{
        return username
    }
    fun setUsername(username: String){
        this.username = username
    }
    //
    fun getImage(): String{
        return  image
    }
    fun setImage(image: String){
        this.image = image
        //
    }
    fun getUid(): String{
        return uid
    }
    fun setUid(uid: String){
        this.uid = uid
    }
    //
    fun getFullName(): String{
        return fullname
    }
    fun setFullName(fullname: String){
        this.fullname = fullname
    }
    //
    fun getAbout(): String{
        return about
    }
    fun setAbout(about: String){
        this.about = about
    }

}