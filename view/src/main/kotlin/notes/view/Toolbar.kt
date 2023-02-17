package notes.view

import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.ColorPicker
import javafx.scene.control.ToolBar
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import notes.model.Model

class Toolbar(val noteModel: Model): ToolBar() {
    val listCollapsableImageView = ImageView(Image("Sidebar-Icon.png"))
    val undoImageView = ImageView(Image("Undo-Icon.png"))
    val redoImageView = ImageView(Image("Redo-Icon.png"))
    val boldImageView = ImageView(Image("Bold-Icon.png"))
    val underlineImageView = ImageView(Image("Underline-Icon.png"))
    val italicizeImageView = ImageView(Image("Italicize-Icon.png"))
    val bulletListImageView = ImageView(Image("Bullet-List-Icon.png"))


    val listCollapsable = Button().apply {
        onMouseClicked = EventHandler {
            if ( noteModel.isSplitView.value ) { // split view
                noteModel.isSplitView.set( false )
            } else { // not split view
                noteModel.isSplitView.set( true )
            }
        }
    }
    val undoButton = Button()
    val redoButton = Button()
    val boldButton = Button()
    val underlineButton = Button()
    val italicizeButton = Button()
    val textColorPicker = ColorPicker()
    val bulletListButton = Button()
    init {
        listCollapsableImageView.fitHeight = 20.0
        listCollapsableImageView.isPreserveRatio = true
        listCollapsable.setPrefSize(20.0, 20.0)
        listCollapsable.graphic = listCollapsableImageView

        undoImageView.fitHeight = 20.0
        undoImageView.isPreserveRatio = true
        undoButton.setPrefSize(20.0, 20.0)
        undoButton.graphic = undoImageView

        redoImageView.fitHeight = 20.0
        redoImageView.isPreserveRatio = true
        redoButton.setPrefSize(20.0, 20.0)
        redoButton.graphic = redoImageView

        boldImageView.fitHeight = 20.0
        boldImageView.isPreserveRatio = true
        boldButton.setPrefSize(20.0, 20.0)
        boldButton.graphic = boldImageView

        underlineImageView.fitHeight = 20.0
        underlineImageView.isPreserveRatio = true
        underlineButton.setPrefSize(20.0, 20.0)
        underlineButton.graphic = underlineImageView

        italicizeImageView.fitHeight = 20.0
        italicizeImageView.isPreserveRatio = true
        italicizeButton.setPrefSize(20.0, 20.0)
        italicizeButton.graphic = italicizeImageView

        bulletListImageView.fitHeight = 20.0
        bulletListImageView.isPreserveRatio = true
        bulletListButton.setPrefSize(20.0, 20.0)
        bulletListButton.graphic = bulletListImageView

        this.items.addAll(listCollapsable, undoButton, redoButton, boldButton, underlineButton, italicizeButton, textColorPicker, bulletListButton)
    }
}