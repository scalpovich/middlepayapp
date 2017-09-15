$(document).ready(function () {
    $("#operateAddress").on("click",function(){
       // $("#operateAddress").text("");//点击的时候删掉P标签的文字
        var geolocation = new BMap.Geolocation();
        geolocation.getCurrentPosition(function(r){
            if(this.getStatus() == BMAP_STATUS_SUCCESS){
                mk = new BMap.Marker(r.point);
                getAddress(r.point);
            }else {
                alert('failed'+this.getStatus());
            }
        });

        //获取地址信息，设置地址label

        function getAddress(point){
            var gc = new BMap.Geocoder();
            gc.getLocation(point, function(rs){
                var addComp = rs.addressComponents;
                
                //省份地址
                var addCompProvince = addComp.province;
                //城市地址
                var addCompCity = addComp.ctiy;
               
                var address =  addComp.province +  addComp.city + addComp.district + addComp.street + addComp.streetNumber;//获取地址

                $("#city").val(addComp.city);
                $("#province").val(addCompProvince);
                $("#Region").val(addComp.district);
                
                //获取到地址后的样式变更
                $("#operateAddress").text(address);
                $("#operateAddress").css("color","#000");
                var merchantName = $("#merchantName").val();
                if(merchantName){
                    $("#next").addClass("active");
                }
            });
        }
    })
});