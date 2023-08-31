package kr.or.iei.myPage.service;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.or.iei.myPage.dao.MyPageDao;
import kr.or.iei.myPage.vo.LikeListData;
import kr.or.iei.myPage.vo.Order;
import kr.or.iei.myPage.vo.OrderCancelListData;
import kr.or.iei.myPage.vo.OrderListData;
import kr.or.iei.product.model.vo.ProductListData;

@Service
public class MyPageService {
	@Autowired
	private MyPageDao myPageDao;

	//전체 주문 내역
	public OrderListData selectAllOrderList(int reqPage) {
		//게시판 페이지는 가장 마지막에 insert된 요소가 제일 상단에 위치하는 내림차순 정렬로 표기
		int numPerPage = 10; //한 페이지에 표시되는 게시물 수를 10개로 설정
		int end = reqPage * numPerPage; //끝나는 개수 숫자 /한 페이지에 표시되는 마지막 숫자 /reqPage가 1일 경우: 10
		int start = end - numPerPage +1; //시작하는 개수 숫자 /한 페이지에 표시되는 시작 숫자/reqPage가 1일 경우: 1
		List orderList = myPageDao.selectAllOrderList(start, end);
		
		//pageNavi 제작준비
		int totalCount = myPageDao.selectOrderTotalCount();
		int totalPage = (int)Math.ceil(totalCount/(double)numPerPage);
		//pageNavi 사이즈(넘버 갯수 지정)
		int pageNaviSize = 5;
		int pageNo = ((reqPage-1)/pageNaviSize)*pageNaviSize+1;
		//pageNavi 제작
		String pageNavi = "<ul class='page-design circle-style'>";
		if(pageNo != 1) {
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/myPage/orderHistory-deliveryStatus_1?btn=1&reqPage="+1+"'>";//1번페이지로 
			pageNavi += "<span class='material-icons'>first_page</span>";//(|<)이렇게 생김
			pageNavi += "</a>";
			pageNavi += "</li>";
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/myPage/orderHistory-deliveryStatus_1?btn=1&reqPage="+(pageNo-1)+"'>";
			pageNavi += "<span class='material-icons'>chevron_left</span>";
			pageNavi += "</a>";
			pageNavi += "</li>";
		}
		//pageNavi 숫자
		for(int i=0;i<pageNaviSize;i++) {
			if(pageNo == reqPage) {
				pageNavi += "<li>";
				pageNavi += "<a class='page-item active-page' href='/myPage/orderHistory-deliveryStatus_1?btn=1&reqPage="+pageNo+"'>";
				pageNavi += pageNo;
				pageNavi += "</a>";
				pageNavi += "</li>";
			}else {
				pageNavi += "<li>";
				pageNavi += "<a class='page-item' href='/myPage/orderHistory-deliveryStatus_1?btn=1&reqPage="+pageNo+"'>";
				pageNavi += pageNo;
				pageNavi += "</a>";
				pageNavi += "</li>";
			}
			pageNo++;
			if(pageNo>totalPage) {
				//페이지가 마지막 페이지에 도달했을 경우
				break;
			}
		}
		if(pageNo <= totalPage) {
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/myPage/orderHistory-deliveryStatus_1?btn=1&reqPage="+pageNo+"'>";
			pageNavi += "<span class='material-icons'>chevron_right</span>";
			pageNavi += "</a>";
			pageNavi += "</li>";
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/myPage/orderHistory-deliveryStatus_1?btn=1&reqPage="+totalPage+"'>";//마지막 페이지로 가기
			pageNavi += "<span class='material-icons'>last_page</span>";//마지막 페이지로 가기 icon
			pageNavi += "</a>";
			pageNavi += "</li>";
		}
		pageNavi += "</ul>";
		
		OrderListData old = new OrderListData(orderList, pageNavi, totalCount);
		return old;
	}//selectAllOrderList()

	//기간내의 주문 내역
	public OrderListData selectDateOrderList(int reqPage, String startDate, String endDate) {
		//게시판 페이지는 가장 마지막에 insert된 요소가 제일 상단에 위치하는 내림차순 정렬로 표기
		int numPerPage = 10; //한 페이지에 표시되는 게시물 수를 10개로 설정
		int end = reqPage * numPerPage; //끝나는 개수 숫자 /한 페이지에 표시되는 마지막 숫자 /reqPage가 1일 경우: 10
		int start = end - numPerPage +1; //시작하는 개수 숫자 /한 페이지에 표시되는 시작 숫자/reqPage가 1일 경우: 1
		List orderList = myPageDao.selectDateOrderList(start, end, startDate, endDate);
//		System.out.println(orderList);
		//pageNavi 제작준비
		int totalCount = myPageDao.selectOrderTotalCount(startDate, endDate);
		int totalPage = (int)Math.ceil(totalCount/(double)numPerPage);
		//pageNavi 사이즈(넘버 갯수 지정)
		int pageNaviSize = 5;
		int pageNo = ((reqPage-1)/pageNaviSize)*pageNaviSize+1;
		//pageNavi 제작
		String pageNavi = "<ul class='page-design circle-style'>";
		if(pageNo != 1) {
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/myPage/orderHistory-deliveryStatus_1?startDate="+startDate+"&endDate="+endDate+"&btn=1&reqPage="+1+"'>";//1번페이지로 
			pageNavi += "<span class='material-icons'>first_page</span>";//(|<)이렇게 생김
			pageNavi += "</a>";
			pageNavi += "</li>";
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/myPage/orderHistory-deliveryStatus_1?startDate="+startDate+"&endDate="+endDate+"&?btn=1&reqPage="+(pageNo-1)+"'>";
			pageNavi += "<span class='material-icons'>chevron_left</span>";
			pageNavi += "</a>";
			pageNavi += "</li>";
		}
		//pageNavi 숫자
		for(int i=0;i<pageNaviSize;i++) {
			if(pageNo == reqPage) {
				pageNavi += "<li>";
				pageNavi += "<a class='page-item active-page' href='/myPage/orderHistory-deliveryStatus_1?startDate="+startDate+"&endDate="+endDate+"&btn=1&reqPage="+pageNo+"'>";
				pageNavi += pageNo;
				pageNavi += "</a>";
				pageNavi += "</li>";
			}else {
				pageNavi += "<li>";
				pageNavi += "<a class='page-item' href='/myPage/orderHistory-deliveryStatus_1?startDate="+startDate+"&endDate="+endDate+"&btn=1&reqPage="+pageNo+"'>";
				pageNavi += pageNo;
				pageNavi += "</a>";
				pageNavi += "</li>";
			}
			pageNo++;
			if(pageNo>totalPage) {
				//페이지가 마지막 페이지에 도달했을 경우
				break;
			}
		}
		if(pageNo <= totalPage) {
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/myPage/orderHistory-deliveryStatus_1?startDate="+startDate+"&endDate="+endDate+"&btn=1&reqPage="+pageNo+"'>";
			pageNavi += "<span class='material-icons'>chevron_right</span>";
			pageNavi += "</a>";
			pageNavi += "</li>";
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/myPage/orderHistory-deliveryStatus_1?startDate="+startDate+"&endDate="+endDate+"&btn=1&reqPage="+totalPage+"'>";//마지막 페이지로 가기
			pageNavi += "<span class='material-icons'>last_page</span>";//마지막 페이지로 가기 icon
			pageNavi += "</a>";
			pageNavi += "</li>";
		}
		pageNavi += "</ul>";
		
		OrderListData old = new OrderListData(orderList, pageNavi, totalCount);
		return old;
	}//selectDateOrderList()
	
	
	
	//주문 내역 상세
	public List selectOrderHistory(Long orderNo) {
		List orderDetail = myPageDao.selectOrderHistory(orderNo);
		return orderDetail;
	}//selectOrderHistory(int orderNo)
	
	


	//전체 주문 내역 - 주문취소/교환/반품
	public OrderListData selectAllOrderList2(int reqPage) {
		//게시판 페이지는 가장 마지막에 insert된 요소가 제일 상단에 위치하는 내림차순 정렬로 표기
		int numPerPage = 10; //한 페이지에 표시되는 게시물 수를 10개로 설정
		int end = reqPage * numPerPage; //끝나는 개수 숫자 /한 페이지에 표시되는 마지막 숫자 /reqPage가 1일 경우: 10
		int start = end - numPerPage +1; //시작하는 개수 숫자 /한 페이지에 표시되는 시작 숫자/reqPage가 1일 경우: 1
		List orderList = myPageDao.selectAllOrderList(start, end);
		
		//pageNavi 제작준비
		int totalCount = myPageDao.totalCount(); //이전코드: selectOrderTotalCount();
		int totalPage = (int)Math.ceil(totalCount/(double)numPerPage);
		//pageNavi 사이즈(넘버 갯수 지정)
		int pageNaviSize = 5;
		int pageNo = ((reqPage-1)/pageNaviSize)*pageNaviSize+1;
		//pageNavi 제작
		String pageNavi = "<ul class='page-design2 circle-style'>";
		if(pageNo != 1) {
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/myPage/orderCancel-change-return_1?btn=2&reqPage="+1+"'>";//1번페이지로 
			pageNavi += "<span class='material-icons'>first_page</span>";//(|<)이렇게 생김
			pageNavi += "</a>";
			pageNavi += "</li>";
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/myPage/orderCancel-change-return_1?btn=2&reqPage="+(pageNo-1)+"'>";
			pageNavi += "<span class='material-icons'>chevron_left</span>";
			pageNavi += "</a>";
			pageNavi += "</li>";
		}
		//pageNavi 숫자
		for(int i=0;i<pageNaviSize;i++) {
			if(pageNo == reqPage) {
				pageNavi += "<li>";
				pageNavi += "<a class='page-item active-page' href='/myPage/orderCancel-change-return_1?btn=2&reqPage="+pageNo+"'>";
				pageNavi += pageNo;
				pageNavi += "</a>";
				pageNavi += "</li>";
			}else {
				pageNavi += "<li>";
				pageNavi += "<a class='page-item' href='/myPage/orderCancel-change-return_1?btn=2&reqPage="+pageNo+"'>";
				pageNavi += pageNo;
				pageNavi += "</a>";
				pageNavi += "</li>";
			}
			pageNo++;
			if(pageNo>totalPage) {
				//페이지가 마지막 페이지에 도달했을 경우
				break;
			}
		}
		if(pageNo <= totalPage) {
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/myPage/orderCancel-change-return_1?btn=2&reqPage="+pageNo+"'>";
			pageNavi += "<span class='material-icons'>chevron_right</span>";
			pageNavi += "</a>";
			pageNavi += "</li>";
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/myPage/orderCancel-change-return_1?btn=2&reqPage="+totalPage+"'>";//마지막 페이지로 가기
			pageNavi += "<span class='material-icons'>last_page</span>";//마지막 페이지로 가기 icon
			pageNavi += "</a>";
			pageNavi += "</li>";
		}
		pageNavi += "</ul>";
		
		OrderListData old = new OrderListData(orderList, pageNavi, totalCount);
		return old;
	}//selectAllOrderList2(int reqPage)
	
	//주문내역 조회 - 주문 번호로 조회
	@Transactional
	public List selectNoOrderList(String orderNo) {
		StringTokenizer st = new StringTokenizer(orderNo, "/");
		/*
		int orderNO = Integer.parseInt(st.nextToken());
		System.out.println(orderNO);
		int orderNO2 = Integer.parseInt(st.nextToken());
		System.out.println(orderNO2);
		Order order1 = myPageDao.selectNoOrderList(orderNO);
		System.out.println(order1);
		Order order2 = myPageDao.selectNoOrderList(orderNO2);
		System.out.println(order2);
		List orderList = new ArrayList<Order>();
		orderList.add(order1);
		orderList.add(order2);
		for(int i=0;i<orderList.size();i++) {
			System.out.println(orderList.get(i));
		}
		*/
		List orderList = new ArrayList<Order>();
		while(st.hasMoreTokens()) {
			int orderNO = Integer.parseInt(st.nextToken());
			Order order = myPageDao.selectNoOrderList(orderNO);
			orderList.add(order);
		}
		return orderList;
	}//selectNoOrderList(String orderNo)

	//찜목록 불러오기
	public LikeListData selectAllListList(int reqPage) {
//		System.out.println(reqPage);
		//게시판 페이지는 가장 마지막에 insert된 요소가 제일 상단에 위치하는 내림차순 정렬로 표기
		int numPerPage = 5; //한 페이지에 표시되는 게시물 수를 10개로 설정
		int end = reqPage * numPerPage; //끝나는 개수 숫자 /한 페이지에 표시되는 마지막 숫자 /reqPage가 1일 경우: 10
		int start = end - numPerPage +1; //시작하는 개수 숫자 /한 페이지에 표시되는 시작 숫자/reqPage가 1일 경우: 1
//		System.out.println("start: "+start);
//		System.out.println("end: "+end);
		List likeList = myPageDao.selectAllLikeList(start, end);
		
		
		//pageNavi 제작준비
		int totalCount = myPageDao.selectLikeListTotalCount();
		int totalPage = (int)Math.ceil(totalCount/(double)numPerPage);
		//pageNavi 사이즈(넘버 갯수 지정)
		int pageNaviSize = 5;
		int pageNo = ((reqPage-1)/pageNaviSize)*pageNaviSize+1;
		//pageNavi 제작
		String pageNavi = "<ul class='page-design circle-style'>";
		if(pageNo != 1) {
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/myPage/likeItList?btn=3&reqPage="+1+"'>";//1번페이지로 
			pageNavi += "<span class='material-icons'>first_page</span>";//(|<)이렇게 생김
			pageNavi += "</a>";
			pageNavi += "</li>";
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/myPage/likeItList?btn=3&reqPage="+(pageNo-1)+"'>";
			pageNavi += "<span class='material-icons'>chevron_left</span>";
			pageNavi += "</a>";
			pageNavi += "</li>";
		}
		//pageNavi 숫자
		for(int i=0;i<pageNaviSize;i++) {
			if(pageNo == reqPage) {
				pageNavi += "<li>";
				pageNavi += "<a class='page-item active-page' href='/myPage/likeItList?btn=3&reqPage="+pageNo+"'>";
				pageNavi += pageNo;
				pageNavi += "</a>";
				pageNavi += "</li>";
			}else {
				pageNavi += "<li>";
				pageNavi += "<a class='page-item' href='/myPage/likeItList?btn=3&reqPage="+pageNo+"'>";
				pageNavi += pageNo;
				pageNavi += "</a>";
				pageNavi += "</li>";
			}
			pageNo++;
			if(pageNo>totalPage) {
				//페이지가 마지막 페이지에 도달했을 경우
				break;
			}
		}
		if(pageNo <= totalPage) {
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/myPage/likeItList?btn=3&reqPage="+pageNo+"'>";
			pageNavi += "<span class='material-icons'>chevron_right</span>";
			pageNavi += "</a>";
			pageNavi += "</li>";
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/myPage/likeItList?btn=3&reqPage="+totalPage+"'>";//마지막 페이지로 가기
			pageNavi += "<span class='material-icons'>last_page</span>";//마지막 페이지로 가기 icon
			pageNavi += "</a>";
			pageNavi += "</li>";
		}
		pageNavi += "</ul>";
		
		LikeListData lld = new LikeListData(likeList, pageNavi, totalCount);
//		System.out.println("servie: "+lld.getLikeList());
		return lld;
	}

	//상품 후기 목록
	public ProductListData selectAllreview(int reqPage) {
		//게시판 페이지는 가장 마지막에 insert된 요소가 제일 상단에 위치하는 내림차순 정렬로 표기
		int numPerPage = 10; //한 페이지에 표시되는 게시물 수를 10개로 설정
		int end = reqPage * numPerPage; //끝나는 개수 숫자 /한 페이지에 표시되는 마지막 숫자 /reqPage가 1일 경우: 10
		int start = end - numPerPage +1; //시작하는 개수 숫자 /한 페이지에 표시되는 시작 숫자/reqPage가 1일 경우: 1
		List reviewList = myPageDao.selectAllreview(start, end);
		
		//pageNavi 제작준비
		int totalCount = myPageDao.selectReviewTotalCount();
		int totalPage = (int)Math.ceil(totalCount/(double)numPerPage);
		//pageNavi 사이즈(넘버 갯수 지정)
		int pageNaviSize = 5;
		int pageNo = ((reqPage-1)/pageNaviSize)*pageNaviSize+1;
		//pageNavi 제작
		String pageNavi = "<ul class='page-design circle-style'>";
		if(pageNo != 1) {
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/myPage/productReview_1?btn=4&reqPage="+1+"'>";//1번페이지로 
			pageNavi += "<span class='material-icons'>first_page</span>";//(|<)이렇게 생김
			pageNavi += "</a>";
			pageNavi += "</li>";
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/myPage/productReview_1?btn=4&reqPage="+(pageNo-1)+"'>";
			pageNavi += "<span class='material-icons'>chevron_left</span>";
			pageNavi += "</a>";
			pageNavi += "</li>";
		}
		//pageNavi 숫자
		for(int i=0;i<pageNaviSize;i++) {
			if(pageNo == reqPage) {
				pageNavi += "<li>";
				pageNavi += "<a class='page-item active-page' href='/myPage/productReview_1?btn=4&reqPage="+pageNo+"'>";
				pageNavi += pageNo;
				pageNavi += "</a>";
				pageNavi += "</li>";
			}else {
				pageNavi += "<li>";
				pageNavi += "<a class='page-item' href='/myPage/productReview_1?btn=4&reqPage="+pageNo+"'>";
				pageNavi += pageNo;
				pageNavi += "</a>";
				pageNavi += "</li>";
			}
			pageNo++;
			if(pageNo>totalPage) {
				//페이지가 마지막 페이지에 도달했을 경우
				break;
			}
		}
		if(pageNo <= totalPage) {
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/myPage/productReview_1?btn=4&reqPage="+pageNo+"'>";
			pageNavi += "<span class='material-icons'>chevron_right</span>";
			pageNavi += "</a>";
			pageNavi += "</li>";
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/myPage/productReview_1?btn=4&reqPage="+totalPage+"'>";//마지막 페이지로 가기
			pageNavi += "<span class='material-icons'>last_page</span>";//마지막 페이지로 가기 icon
			pageNavi += "</a>";
			pageNavi += "</li>";
		}
		pageNavi += "</ul>";
		
		ProductListData pld = new ProductListData(reviewList, pageNavi, totalCount);
		return pld;
	}

	//주문취소/교환/반품 등록
	@Transactional
	public int insertOrderCancelList(int orderStatus, String reasonDetail, Long orderNo, String productName, String optionName, int orderCount, int memberNo) {
		System.out.println("orderNo : "+orderNo);
		int result = myPageDao.insertOrderCancelList(orderStatus, reasonDetail, orderNo, productName, optionName, orderCount, memberNo);
		System.out.println("result: "+result);
		return result;
	}
	//주문취소/교환/반품 내역 출력
	public OrderCancelListData selectAllOrderCancel(int reqPage) {
		//게시판 페이지는 가장 마지막에 insert된 요소가 제일 상단에 위치하는 내림차순 정렬로 표기
		int numPerPage = 10; //한 페이지에 표시되는 게시물 수를 10개로 설정
		int end = reqPage * numPerPage; //끝나는 개수 숫자 /한 페이지에 표시되는 마지막 숫자 /reqPage가 1일 경우: 10
		int start = end - numPerPage +1; //시작하는 개수 숫자 /한 페이지에 표시되는 시작 숫자/reqPage가 1일 경우: 1
		List cancelList = myPageDao.selectAllOrderCancel(start, end);
		
		//pageNavi 제작준비
		int totalCount = myPageDao.selectCancelTotalCount();
		int totalPage = (int)Math.ceil(totalCount/(double)numPerPage);
		//pageNavi 사이즈(넘버 갯수 지정)
		int pageNaviSize = 5;
		int pageNo = ((reqPage-1)/pageNaviSize)*pageNaviSize+1;
		//pageNavi 제작
		String pageNavi = "<ul class='page-design3 circle-style'>";
		if(pageNo != 1) {
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/myPage/orderCancel-change-return_3?btn=2&reqPage="+1+"'>";//1번페이지로 
			pageNavi += "<span class='material-icons'>first_page</span>";//(|<)이렇게 생김
			pageNavi += "</a>";
			pageNavi += "</li>";
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/myPage/orderCancel-change-return_3?btn=2&reqPage="+(pageNo-1)+"'>";
			pageNavi += "<span class='material-icons'>chevron_left</span>";
			pageNavi += "</a>";
			pageNavi += "</li>";
		}
		//pageNavi 숫자
		for(int i=0;i<pageNaviSize;i++) {
			if(pageNo == reqPage) {
				pageNavi += "<li>";
				pageNavi += "<a class='page-item active-page' href='/myPage/orderCancel-change-return_3?btn=2&reqPage="+pageNo+"'>";
				pageNavi += pageNo;
				pageNavi += "</a>";
				pageNavi += "</li>";
			}else {
				pageNavi += "<li>";
				pageNavi += "<a class='page-item' href='/myPage/orderCancel-change-return_3?btn=2&reqPage="+pageNo+"'>";
				pageNavi += pageNo;
				pageNavi += "</a>";
				pageNavi += "</li>";
			}
			pageNo++;
			if(pageNo>totalPage) {
				//페이지가 마지막 페이지에 도달했을 경우
				break;
			}
		}
		if(pageNo <= totalPage) {
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/myPage/orderCancel-change-return_3?btn=2&reqPage="+pageNo+"'>";
			pageNavi += "<span class='material-icons'>chevron_right</span>";
			pageNavi += "</a>";
			pageNavi += "</li>";
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/myPage/orderCancel-change-return_3?btn=2&reqPage="+totalPage+"'>";//마지막 페이지로 가기
			pageNavi += "<span class='material-icons'>last_page</span>";//마지막 페이지로 가기 icon
			pageNavi += "</a>";
			pageNavi += "</li>";
		}
		pageNavi += "</ul>";
		
		OrderCancelListData ocld = new OrderCancelListData(cancelList, pageNavi, totalCount);
		return ocld;
	}

	//주문내역에서 삭제
	public int deleteOrderHistory(Long orderNo) {
		int result = myPageDao.deleteOrderHistory(orderNo);
		return result;
	}



}//MyPageService
