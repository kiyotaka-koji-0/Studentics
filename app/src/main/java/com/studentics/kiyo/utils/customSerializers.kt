import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.studentics.kiyo.viewModels.Exam
import java.lang.reflect.Type

class ExamSerializer : JsonSerializer<Exam> {
    override fun serialize(src: Exam, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty("name", src.name)
        jsonObject.addProperty("className", src.className)
        jsonObject.addProperty("datetime", src.datetime.toString())
        jsonObject.addProperty("aiReview", src.aiReview)
        jsonObject.add("subjects", context.serialize(src.subjects))
        return jsonObject
    }
}

val datetimeGson = GsonBuilder()
    .registerTypeAdapter(Exam::class.java, ExamSerializer())
    .create()


