package com.example.galleryservice.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.galleryservice.model.Gallery;
import com.example.galleryservice.model.Student;

@RestController
public class StudentController {

	@Autowired
	Environment environment;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private LoadBalancerClient loadBalancer;

	@GetMapping("/name")
	public String getControllerName() {
		System.out.println("Inside MyRestController::backend...");

		String serverPort = environment.getProperty("local.server.port");

		System.out.println("Port : " + serverPort);
		String serviceId = "image-service".toLowerCase();
		ServiceInstance serviceInstance = this.loadBalancer.choose(serviceId);

		String url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/images";
		// get list of available images
		//List<Object> images = restTemplate.getForObject(url, List.class);
		List<Object> images = restTemplate.getForObject("http://image-service/images/", List.class);

		return "Hello form Backend!!! " + " Host : localhost " + " :: Port : " + serverPort;
	}

	@GetMapping("/students/{student_id}")
	public Student getStudentById(@PathVariable("student_id") Integer studentId) {
		return new Student(1, "Chathuranga", "Bsc", "Sri Lanka");
	}

	@RequestMapping("/{id}")
	public Gallery getGallery(@PathVariable final int id) {
		// create gallery object
		Gallery gallery = new Gallery();
		gallery.setId(id);
		String serviceId = "image-service".toLowerCase();
		ServiceInstance serviceInstance = this.loadBalancer.choose(serviceId);

		String url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/images";
		// get list of available images
		//List<Object> images = restTemplate.getForObject(url, List.class);
		List<Object> images = restTemplate.getForObject("http://image-service/images/", List.class);
		
		// get list of available images
		//List<Object> images = restTemplate.getForObject("http://image-service/images/", List.class);
		gallery.setImages(images);
		System.out.println(images);
		return gallery;
	}

	@GetMapping("/courses/{course_id}/students")
	public List<Student> getStudentsByCourses(@PathVariable("course_id") Integer courseId) {
		List<Student> studentList = new ArrayList<>();

		studentList.add(new Student(1, "Chathuranga", "Bsc", "Sri Lanka"));
		studentList.add(new Student(2, "Darshana", "Sun Certified", "Sri Lanka"));
		return studentList;
	}

	@GetMapping("/departments/{department_id}/courses/{course_id}/students")
	public List<Student> getStudentsByDepartmentCourses(@PathVariable("department_id") Integer departmentId,
			@PathVariable("course_id") Integer courseId) {
		List<Student> studentList = new ArrayList<>();

		studentList.add(new Student(1, "Chathuranga", "Bsc", "Sri Lanka"));
		studentList.add(new Student(2, "Darshana", "Sun Certified", "Sri Lanka"));
		studentList.add(new Student(3, "Tennakoon", "Zend Certified", "Sri Lanka"));
		return studentList;
	}
}