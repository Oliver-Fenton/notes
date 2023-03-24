package notes.shared.webserviceclient

import notes.shared.model.NoteData
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class WebServiceClientTest {
    private val client = WebServiceClient()
    @Disabled
    @Test
    fun getNotes() { println(client.get()) }
    @Disabled
    @Test
    fun getNote() { println(client.get(1)) }
    @Disabled
    @Test
    fun postNote() { println(client.post(NoteData(1, "note 1", "1 1 1 1", "2023-03-20T10:30:15.123456", "2023-03-20T10:30:15.123456", "").toJson())) }
    @Disabled
    @Test
    fun putNote() { println(client.post(NoteData(1, "new note 1", "new body", "2023-03-20T10:30:15.123456", "2023-03-20T10:30:15.123456", "").toJson())) }
    @Disabled
    @Test
    fun deleteNote() { println(client.delete(1)) }
    @Disabled
    @Test
    fun testClient() {
        val client = WebServiceClient()
        println(client.get())
        println(client.post(NoteData(1, "note 1", "1 1 1 1", "2023-03-20T10:30:15.123456", "2023-03-20T10:30:15.123456", "").toJson()))
        println(client.get())
        println(client.put(1, NoteData(1, "new title", "new body", "2023-03-20T10:30:15.123456", "2023-03-20T10:30:15.123456", "").toJson()))
        println(client.delete(1))
        println(client.get())
    }
}