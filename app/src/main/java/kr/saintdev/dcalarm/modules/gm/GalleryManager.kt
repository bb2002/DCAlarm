package kr.saintdev.dcalarm.modules.gm

class GalleryManager {
    companion object {
        private var ins: GalleryManager? = null

        fun getInstance() : GalleryManager {
            if(ins == null) {
                ins = GalleryManager()
            }

            return ins!!
        }
    }

    private constructor() {

    }
}