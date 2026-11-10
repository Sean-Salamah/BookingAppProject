package com.example.tusbookings.data

import com.example.tusbookings.model.Equipment

import com.example.tusbookings.model.Category



val sampleCategories = listOf(

    Category(1, "Laptop"),
    Category(2, "Camera"),
    Category(3, "Microphone"),
    Category(4, "Speaker"),
    Category(5, "Sensor")

)


val sampleEquipment = listOf(

    Equipment(
        id = 1, name = "Dell Laptop", categoryId = 1,
        description = " A Dell XPS  with 16 gb of ram"
    ),
    Equipment(
        id = 2, name = "MacBook", categoryId = 1,
        description = "a MacBook with 16gbs of ram"
    ),
    Equipment(
        id = 3, name = "Sony Camera", categoryId = 2,
        description = "A Sony Camera with a high zoom lens"
    ),
    Equipment(
        id = 4, name = "Blue Yeti Microphone", categoryId = 3,
        description = "A Blue Yeti Microphone with good audio quality"
    ),
    Equipment(
        id = 5, name = "Sony Speaker", categoryId = 4,
        description = "A Sony Speaker with good audio quality"
    ),
    Equipment(
        id = 6, name = "Temp Sensor", categoryId = 5,
        description = "A Digital temperature sensor"
    ),



)
