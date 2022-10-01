package com.example.smartsaklar.schedule

class ModelBarangSchedule {

    var key: String? = null
//    var nama: String? = null
//    var lampu1: String? = null
//    var lampu2: String? = null

    var Time: String? = null
    var State: String? = null

    constructor() {}

    constructor(time: String?,state: String? ) {
        Time = time
        State = state
//        lampu2 = lampuRuangan2
//        lampu = lampuRuangan3
//        merk = merkBarang
//        harga = hargaBarang
    }
}