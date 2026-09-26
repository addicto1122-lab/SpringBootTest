package com.example.ch2;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

@SpringBootApplication
public class Ch2Application implements CommandLineRunner {

	@Autowired
	EntityManagerFactory entityManagerFactory;

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Ch2Application.class);
		app.run(args);
	}

	@Override
	public void run(String... args) throws Exception{
		System.out.println("entityManagerFactory = " + entityManagerFactory);
		// 엔티티 매니저
		EntityManager em1 = entityManagerFactory.createEntityManager();
		EntityManager em2 = entityManagerFactory.createEntityManager();
		System.out.println("em1 = " + em1); //SessionImpl(2006562550<open>)
		System.out.println("em2 = " + em2); //SessionImpl(437872452<open>)


//		User user = new User();
//		user.setId("aaa");
//		user.setEmail("bbb");
//		user.setPwd("1234");
//		user.setName("홍길동");
//		user.setReg_date(new Date());
//
//		EntityTransaction tx = em1.getTransaction();
//		tx.begin(); // 트랜젝션 시작
//		// user 객체 저장
//		em1.persist(user); // 엔티티를 PersistentContext에 등록
//		// 변경
//		user.setBirth("26-09-21");
//		tx.commit(); // 트래젝션 종료 (PersistentContext -> DB에 반영)
//
//		System.out.println("user: "+ user);
//
//		em1.clear();
//
//		// 조회
//		// PersistenceContext에 등록되어 있다면 쿼리 생성 X (효율적인 처리를 위해 )
//		User user2 = em1.find(User.class, "aaa");
//		System.out.println("user == user2 = " + (user == user2));
//
//		User user3 = em1.find(User.class, "ccc"); // 쿼리 생성 O
//		System.out.println("user == user3 = " + (user == user3));

		// user 객체 삭제
//		em1.remove(user); //em1에서 삭제
//		tx.begin();
//		em1.remove(user);
//		tx.commit(); // DB에 반영
	}
}
