package me.sedlar.util

import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Parent

object JFX {

    fun loadFXML(file: String): Node {
        return FXMLLoader.load<Parent>(javaClass.getResource(file))
    }
}