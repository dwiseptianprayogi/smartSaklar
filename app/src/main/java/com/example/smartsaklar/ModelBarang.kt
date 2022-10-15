package com.example.smartsaklar

class ModelBarang {

    var key: String? = null
    var nama: String? = null
    var lampu1: String? = null
    var lampu2: String? = null
//    var lampu: String? = null
//    var harga: String? = null
    var Time: String? = null
    var State: String? = null

    constructor(){}

    constructor(Key:String?) {
        key = Key
    }

    constructor(stateSchedule:String?, waktuSchedule:String) {
        State = stateSchedule
        Time = waktuSchedule
    }

    constructor(namaBarang: String?,lampuRuangan: String?, lampuRuangan2: String? ) {
        nama = namaBarang
        lampu1 = lampuRuangan
        lampu2 = lampuRuangan2
//        lampu = lampuRuangan3
//        merk = merkBarang
//        harga = hargaBarang
    }
}