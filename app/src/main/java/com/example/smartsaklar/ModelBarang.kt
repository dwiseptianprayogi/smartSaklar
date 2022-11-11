package com.example.smartsaklar

class ModelBarang {

    var key: String? = null
    var nama: String? = null
    var idAlat:String? = null
    var lampu1: String? = null
    var lampu2: String? = null
    var Time: String? = null
    var State: String? = null
    var idStat:String? = null

    var day :String? = null
    var month :String? = null
    var year :String? = null
    var hour :String? = null
    var minute :String? = null


    constructor(){}

    constructor(Key:String?) {
        key = Key
    }

    constructor(stateSchedule:String?, waktuSchedule:String,Day:String,Month:String,Year:String, Hour:String, Minute:String  ) {
        State = stateSchedule
        Time = waktuSchedule
        day = Day
        month = Month
        year = Year
        hour = Hour
        minute = Minute

    }

    constructor(namaBarang: String?, id:String?,lampuRuangan: String?, lampuRuangan2: String?, idStatus:String? ) {
        nama = namaBarang
        idAlat = id
        lampu1 = lampuRuangan
        lampu2 = lampuRuangan2
        idStat = idStatus
    }

//    constructor(namaBarang: String?,lampuRuangan: String?, lampuRuangan2: String? ) {
//        nama = namaBarang
//        lampu1 = lampuRuangan
//        lampu2 = lampuRuangan2
////        lampu = lampuRuangan3
////        merk = merkBarang
////        harga = hargaBarang
//    }
}