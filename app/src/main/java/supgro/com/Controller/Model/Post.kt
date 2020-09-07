package supgro.com.Controller.Model

class Post {

    private var postid: String = ""
    private var caption: String = ""
    private var postimage: String = ""
    private var publisher: String = ""
    private var posttext: String = ""


    constructor()
    constructor(postid: String, caption: String, postimage: String, publisher: String) {
        this.postid = postid
        this.caption = caption
        this.postimage = postimage
        this.publisher = publisher
        this.posttext = posttext

    }
//___________________________________________

    fun getPostId(): String{
        return postid
    }
    fun setPostId(postid: String){
        this.postid = postid
    }
//___________________________________________

    fun getCaption(): String{
        return caption
    }
    fun setCaption(caption: String){
        this.caption = caption
    }
//___________________________________________

    fun getPostImage(): String{
        return postimage
    }
    fun setPostImage(postimage: String){
        this.postimage = postimage
    }
//___________________________________________

    fun getPublisher(): String{
        return publisher
    }
    fun setPublisher(publisher: String){
        this.publisher = publisher
    }
//___________________________________________

    fun getPostText(): String{
        return posttext
    }
    fun setPostText(posttext: String){
        this.posttext = posttext
    }

}