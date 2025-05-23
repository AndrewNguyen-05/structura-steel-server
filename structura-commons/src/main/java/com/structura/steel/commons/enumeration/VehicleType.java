package com.structura.steel.commons.enumeration;

public enum VehicleType {
	/** Xe đầu kéo + Rơ moóc (Sàn, Xương, Ben...) - Phù hợp chở khối lượng lớn, hàng dài, cồng kềnh. */
	TRACTOR_TRAILER,

	/** Xe tải nặng (Thường là 3 chân, 4 chân, 5 chân) - Chở khối lượng lớn, linh hoạt hơn đầu kéo ở một số tuyến đường. */
	HEAVY_TRUCK,

 	/** Xe tải có gắn cẩu - Tiện lợi cho việc bốc dỡ tại công trường hoặc kho không có thiết bị. */
	CRANE_TRUCK,

 	/** Xe tải trung/nhẹ - Dùng cho các đơn hàng nhỏ hơn hoặc vận chuyển nội bộ, tuyến ngắn. */
	MEDIUM_LIGHT_TRUCK,

 	/** Các loại xe chuyên dụng khác (Ví dụ: Moóc rút, Moóc lùn). */
	SPECIALIZED;
}