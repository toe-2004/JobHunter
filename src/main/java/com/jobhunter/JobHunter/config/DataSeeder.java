//package com.jobhunter.JobHunter.config;
//
//import com.jobhunter.JobHunter.enumeration.*;
//import com.jobhunter.JobHunter.model.*;
//import com.jobhunter.JobHunter.repository.*;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.*;
//
//@Component
//public class DataSeeder implements CommandLineRunner {
//
//    private final CategoryRepository categoryRepo;
//    private final SkillRepository skillRepo;
//    private final UserRepository userRepo;
//    private final FreelancerRepository freelancerRepo;
//    private final EmployerRepository employerRepo;
//    private final FreelancerSkillRepository freelancerSkillRepo;
//    private final JobRepository jobRepo;
//    private final JobSkillRepository jobSkillRepo;
//    private final ApplicationRepository applicationRepo;
//    private final EngagementRepository engagementRepo;
//    private final PasswordEncoder passwordEncoder;
//
//    private final Random random = new Random();
//
//    public DataSeeder(
//            CategoryRepository categoryRepo,
//            SkillRepository skillRepo,
//            UserRepository userRepo,
//            FreelancerRepository freelancerRepo,
//            EmployerRepository employerRepo,
//            FreelancerSkillRepository freelancerSkillRepo,
//            JobRepository jobRepo,
//            JobSkillRepository jobSkillRepo,
//            ApplicationRepository applicationRepo,
//            EngagementRepository engagementRepo,
//            PasswordEncoder passwordEncoder) {
//
//        this.categoryRepo = categoryRepo;
//        this.skillRepo = skillRepo;
//        this.userRepo = userRepo;
//        this.freelancerRepo = freelancerRepo;
//        this.employerRepo = employerRepo;
//        this.freelancerSkillRepo = freelancerSkillRepo;
//        this.jobRepo = jobRepo;
//        this.jobSkillRepo = jobSkillRepo;
//        this.applicationRepo = applicationRepo;
//        this.engagementRepo = engagementRepo;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @Override
//    public void run(String... args) {
//
//        System.out.println("=================================");
//        System.out.println("Starting sample data seeding...");
//        System.out.println("=================================");
//
//        seedCategories();
//        seedSkills();
//
//        seedFreelancers();
//        seedEmployers();
//        seedGuests();
//
//        seedJobs();
//        seedApplications();
//        seedEngagements();
//
//        System.out.println("=================================");
//        System.out.println("Sample data seeding completed.");
//        System.out.println("=================================");
//    }
//
//
//    // =========================================================
//    // CATEGORIES
//    // =========================================================
//
//    private void seedCategories() {
//
//        List<String> categories = List.of(
//                "Web Development",
//                "Mobile Development",
//                "UI/UX Design",
//                "Graphic Design",
//                "Data Science",
//                "Machine Learning",
//                "DevOps & Cloud",
//                "Cybersecurity",
//                "Game Development",
//                "Blockchain",
//                "Content Writing",
//                "SEO & Marketing",
//                "Video Editing",
//                "3D Modeling",
//                "Database Administration"
//        );
//
//        for (String name : categories) {
//
//            if (!categoryRepo.existsByName(name)) {
//
//                Category category = new Category();
//                category.setName(name);
//
//                categoryRepo.save(category);
//            }
//        }
//
//        System.out.println("Categories seeded.");
//    }
//
//
//    // =========================================================
//    // SKILLS
//    // =========================================================
//
//    private void seedSkills() {
//
//        List<String> skills = List.of(
//                "Java",
//                "Python",
//                "JavaScript",
//                "TypeScript",
//                "PHP",
//                "C#",
//                "Go",
//                "Rust",
//                "Swift",
//                "Kotlin",
//
//                "React",
//                "Vue.js",
//                "Angular",
//                "HTML",
//                "CSS",
//                "Bootstrap",
//                "Tailwind CSS",
//
//                "Spring Boot",
//                "Django",
//                "Laravel",
//                "Node.js",
//                "Express.js",
//                "FastAPI",
//
//                "Android",
//                "iOS",
//                "Flutter",
//                "React Native",
//
//                "MySQL",
//                "PostgreSQL",
//                "MongoDB",
//                "Redis",
//                "Firebase",
//
//                "Docker",
//                "Kubernetes",
//                "AWS",
//                "Azure",
//                "CI/CD",
//                "Linux",
//
//                "Figma",
//                "Adobe XD",
//                "Photoshop",
//                "Illustrator",
//
//                "Machine Learning",
//                "TensorFlow",
//                "PyTorch",
//                "SQL",
//                "Data Analysis",
//
//                "Git",
//                "REST API",
//                "GraphQL",
//                "Solidity",
//                "WordPress"
//        );
//
//        for (String name : skills) {
//
//            if (!skillRepo.existsByName(name)) {
//
//                Skill skill = new Skill();
//                skill.setName(name);
//
//                skillRepo.save(skill);
//            }
//        }
//
//        System.out.println("Skills seeded.");
//    }
//
//
//    // =========================================================
//    // FREELANCERS
//    // =========================================================
//
//    private void seedFreelancers() {
//
//        List<Freelancer> existingFreelancers =
//                freelancerRepo.findAll();
//
//        Map<String, Freelancer> existingByEmail =
//                new HashMap<>();
//
//        for (Freelancer freelancer : existingFreelancers) {
//
//            if (freelancer.getUser() != null) {
//
//                existingByEmail.put(
//                        freelancer.getUser().getEmail(),
//                        freelancer
//                );
//            }
//        }
//
//        List<Skill> skills = skillRepo.findAll();
//
//        String[] firstNames = {
//                "John",
//                "Emma",
//                "Michael",
//                "Sophia",
//                "Daniel",
//                "Olivia",
//                "James",
//                "Emily",
//                "William",
//                "Ava",
//                "David",
//                "Mia",
//                "Robert",
//                "Isabella",
//                "Thomas",
//                "Amelia",
//                "Alex",
//                "Charlotte",
//                "Henry",
//                "Grace"
//        };
//
//        String[] lastNames = {
//                "Smith",
//                "Johnson",
//                "Brown",
//                "Taylor",
//                "Anderson",
//                "Wilson",
//                "Moore",
//                "Martin",
//                "Jackson",
//                "White",
//                "Harris",
//                "Clark",
//                "Lewis",
//                "Walker",
//                "Hall"
//        };
//
//        String[] titles = {
//                "Java Developer",
//                "Frontend Developer",
//                "Backend Developer",
//                "Full Stack Developer",
//                "UI/UX Designer",
//                "Data Analyst",
//                "Data Scientist",
//                "Mobile App Developer",
//                "DevOps Engineer",
//                "Graphic Designer"
//        };
//
//        String[] locations = {
//                "Yangon",
//                "Mandalay",
//                "Naypyidaw",
//                "Bago",
//                "Mawlamyine",
//                "Taunggyi",
//                "Pathein"
//        };
//
//        int created = 0;
//
//        for (int i = 1; i <= 100; i++) {
//
//            String email =
//                    "freelancer" + i + "@example.com";
//
//            if (existingByEmail.containsKey(email)) {
//                continue;
//            }
//
//            String firstName =
//                    firstNames[random.nextInt(firstNames.length)];
//
//            String lastName =
//                    lastNames[random.nextInt(lastNames.length)];
//
//            String fullName =
//                    firstName + " " + lastName + " " + i;
//
//            User user = new User();
//
//            user.setName(fullName);
//            user.setEmail(email);
//
//            user.setPassword(
//                    passwordEncoder.encode("password123")
//            );
//
//            user.setRole(Role.FREELANCER);
//
//            user.setProfilePhoto(
//                    "default_profile.jpg"
//            );
//
//            userRepo.save(user);
//
//            Freelancer freelancer = new Freelancer();
//
//            freelancer.setUser(user);
//
//            freelancer.setTitle(
//                    titles[random.nextInt(titles.length)]
//            );
//
//            freelancer.setPhone(
//                    "09" +
//                    (100000000 + random.nextInt(900000000))
//            );
//
//            freelancer.setLocation(
//                    locations[random.nextInt(locations.length)]
//            );
//
//            freelancer.setSummary(
//                    "Experienced freelancer with strong skills " +
//                    "in software development, design and " +
//                    "data-related projects."
//            );
//
//            freelancer.setExperience(
//                    (1 + random.nextInt(6)) +
//                    " years of professional experience."
//            );
//
//            freelancer.setEducation(
//                    "Bachelor's Degree in Computer Science"
//            );
//
//            freelancerRepo.save(freelancer);
//
//            assignFreelancerSkills(
//                    freelancer,
//                    skills
//            );
//
//            created++;
//        }
//
//        System.out.println(
//                "Freelancers created: " + created
//        );
//    }
//
//
//    private void assignFreelancerSkills(
//            Freelancer freelancer,
//            List<Skill> skills) {
//
//        if (skills.isEmpty()) {
//            return;
//        }
//
//        List<Skill> shuffled =
//                new ArrayList<>(skills);
//
//        Collections.shuffle(shuffled);
//
//        int skillCount =
//                Math.min(
//                        2 + random.nextInt(4),
//                        shuffled.size()
//                );
//
//        for (int i = 0; i < skillCount; i++) {
//
//            FreelancerSkill freelancerSkill =
//                    new FreelancerSkill();
//
//            freelancerSkill.setFreelancer(freelancer);
//            freelancerSkill.setSkill(shuffled.get(i));
//
//            freelancerSkillRepo.save(
//                    freelancerSkill
//            );
//        }
//    }
//
//
//    // =========================================================
//    // EMPLOYERS
//    // =========================================================
//
//    private void seedEmployers() {
//
//        List<Employer> existingEmployers =
//                employerRepo.findAll();
//
//        Set<String> existingEmails =
//                new HashSet<>();
//
//        for (Employer employer : existingEmployers) {
//
//            if (employer.getUser() != null) {
//
//                existingEmails.add(
//                        employer.getUser().getEmail()
//                );
//            }
//        }
//
//        String[] companies = {
//                "Tech Solutions",
//                "Digital Innovations",
//                "Future Systems",
//                "Smart Technologies",
//                "Global Soft",
//                "Creative Studio",
//                "NextGen IT",
//                "Cloud Works",
//                "Code Factory",
//                "Vision Labs"
//        };
//
//        String[] locations = {
//                "Yangon",
//                "Mandalay",
//                "Naypyidaw",
//                "Bago",
//                "Mawlamyine",
//                "Taunggyi"
//        };
//
//        int created = 0;
//
//        for (int i = 1; i <= 200; i++) {
//
//            String email =
//                    "employer" + i + "@example.com";
//
//            if (existingEmails.contains(email)) {
//                continue;
//            }
//
//            String companyName =
//                    companies[random.nextInt(companies.length)]
//                    + " " + i;
//
//            User user = new User();
//
//            user.setName("Employer " + i);
//            user.setEmail(email);
//
//            user.setPassword(
//                    passwordEncoder.encode("password123")
//            );
//
//            user.setRole(Role.EMPLOYER);
//
//            user.setProfilePhoto(
//                    "default_employer_profile.jpg"
//            );
//
//            userRepo.save(user);
//
//            Employer employer = new Employer();
//
//            employer.setUser(user);
//
//            employer.setCompanyName(
//                    companyName
//            );
//
//            employer.setCompanyEmail(
//                    "company" + i + "@example.com"
//            );
//
//            employer.setCompanyPhone(
//                    "09" +
//                    (100000000 + random.nextInt(900000000))
//            );
//
//            employer.setCompanyDescription(
//                    "A growing company looking for talented " +
//                    "professionals to join our team and work " +
//                    "on exciting projects."
//            );
//
//            employer.setCompanyLocation(
//                    locations[random.nextInt(locations.length)]
//            );
//
//            employerRepo.save(employer);
//
//            created++;
//        }
//
//        System.out.println(
//                "Employers created: " + created
//        );
//    }
//
//
//    // =========================================================
//    // GUESTS
//    // =========================================================
//
//    private void seedGuests() {
//
//        long existingGuests =
//                userRepo.countByRole(Role.GUEST);
//
//        int created = 0;
//
//        for (int i = 1; i <= 50; i++) {
//
//            String email =
//                    "guest" + i + "@example.com";
//
//            if (userRepo.existsByEmail(email)) {
//                continue;
//            }
//
//            User user = new User();
//
//            user.setName("Guest " + i);
//            user.setEmail(email);
//
//            user.setPassword(
//                    passwordEncoder.encode("password123")
//            );
//
//            user.setRole(Role.GUEST);
//
//            user.setProfilePhoto(
//                    "default_profile.jpg"
//            );
//
//            userRepo.save(user);
//
//            created++;
//        }
//
//        System.out.println(
//                "Guests created: " + created
//        );
//    }
//
//
//    // =========================================================
//    // JOBS
//    // =========================================================
//
//    private void seedJobs() {
//
//        if (jobRepo.count() >= 100) {
//
//            System.out.println(
//                    "100 or more jobs already exist. Skipping jobs."
//            );
//
//            return;
//        }
//
//        List<Employer> employers =
//                employerRepo.findAll();
//
//        List<Category> categories =
//                categoryRepo.findAll();
//
//        List<Skill> skills =
//                skillRepo.findAll();
//
//        if (employers.isEmpty()
//                || categories.isEmpty()
//                || skills.isEmpty()) {
//
//            System.out.println(
//                    "Cannot create jobs. Missing employers, " +
//                    "categories or skills."
//            );
//
//            return;
//        }
//
//        String[] jobTitles = {
//                "Senior Java Developer",
//                "Junior Java Developer",
//                "Frontend Developer",
//                "Backend Developer",
//                "Full Stack Developer",
//                "React Developer",
//                "Spring Boot Developer",
//                "Python Developer",
//                "Data Analyst",
//                "Data Scientist",
//                "Machine Learning Engineer",
//                "UI/UX Designer",
//                "Graphic Designer",
//                "Mobile App Developer",
//                "Flutter Developer",
//                "DevOps Engineer",
//                "Cloud Engineer",
//                "Database Administrator",
//                "Cybersecurity Analyst",
//                "WordPress Developer"
//        };
//
//        String[] descriptions = {
//                "We are looking for a talented professional to join our growing team. " +
//                "You will work on exciting projects and collaborate with experienced team members.",
//
//                "Join our development team and help us build modern, scalable and user-friendly applications. " +
//                "Strong communication and problem-solving skills are required.",
//
//                "We are seeking a motivated professional who can contribute to real-world projects " +
//                "and deliver high-quality work within deadlines.",
//
//                "Work with our team to design, develop and maintain innovative software solutions. " +
//                "Experience with modern development tools is preferred."
//        };
//
//        for (int i = 1; i <= 100; i++) {
//
//            Job job = new Job();
//
//            job.setTitle(
//                    jobTitles[random.nextInt(jobTitles.length)]
//            );
//
//            job.setDescription(
//                    descriptions[
//                            random.nextInt(descriptions.length)
//                    ]
//            );
//
//            Employer employer =
//                    employers.get(
//                            random.nextInt(employers.size())
//                    );
//
//            Category category =
//                    categories.get(
//                            random.nextInt(categories.size())
//                    );
//
//            job.setEmployer(employer);
//            job.setCategory(category);
//
//            EmploymentType employmentType =
//                    randomEmploymentType();
//
//            job.setEmploymentType(
//                    employmentType
//            );
//
//            job.setDuration(
//                    randomDuration()
//            );
//
//            job.setExperienceLevel(
//                    randomExperienceLevel()
//            );
//
//            job.setStatus(
//                    randomJobStatus()
//            );
//
//            /*
//             * Salary for regular jobs.
//             */
//            if (employmentType == EmploymentType.FIXED_PRICE
//                    || employmentType == EmploymentType.HOURLY) {
//
//                BigDecimal min =
//                        BigDecimal.valueOf(
//                                500000 +
//                                random.nextInt(1000000)
//                        );
//
//                BigDecimal max =
//                        min.add(
//                                BigDecimal.valueOf(
//                                        100000 +
//                                        random.nextInt(500000)
//                                )
//                        );
//
//                BigDecimal budget =
//                        BigDecimal.valueOf(
//                                500000 +
//                                random.nextInt(1000000)
//                        );
//
//                job.setSalaryMin(min);
//                job.setSalaryMax(max);
//                job.setBudget(budget);
//
//            } else {
//
//                /*
//                 * Budget for hourly / freelance jobs.
//                 */
//                BigDecimal budget =
//                        BigDecimal.valueOf(
//                                100000 +
//                                random.nextInt(900000)
//                        );
//
//                job.setBudget(budget);
//
//                job.setSalaryMin(null);
//                job.setSalaryMax(null);
//            }
//
//            job.setDeadline(
//                    LocalDateTime.now()
//                            .plusDays(
//                                    7 + random.nextInt(45)
//                            )
//            );
//
//            job.setCreatedAt(
//                    LocalDateTime.now()
//                            .minusDays(
//                                    random.nextInt(30)
//                            )
//            );
//
//            Job savedJob =
//                    jobRepo.save(job);
//
//            assignJobSkills(
//                    savedJob,
//                    skills
//            );
//        }
//
//        System.out.println("100 jobs seeded.");
//    }
//
//
//    private void assignJobSkills(
//            Job job,
//            List<Skill> skills) {
//
//        if (skills.isEmpty()) {
//            return;
//        }
//
//        List<Skill> shuffled =
//                new ArrayList<>(skills);
//
//        Collections.shuffle(shuffled);
//
//        int skillCount =
//                Math.min(
//                        2 + random.nextInt(4),
//                        shuffled.size()
//                );
//
//        for (int i = 0; i < skillCount; i++) {
//
//            JobSkill jobSkill =
//                    new JobSkill();
//
//            jobSkill.setJob(job);
//            jobSkill.setSkill(
//                    shuffled.get(i)
//            );
//
//            jobSkillRepo.save(jobSkill);
//        }
//    }
//
//
//    // =========================================================
//    // APPLICATIONS
//    // =========================================================
//
//    private void seedApplications() {
//
//        if (jobRepo.count() == 0
//                || freelancerRepo.count() == 0) {
//
//            System.out.println(
//                    "Cannot create applications."
//            );
//
//            return;
//        }
//
//        List<Job> jobs =
//                jobRepo.findAll();
//
//        List<Freelancer> freelancers =
//                freelancerRepo.findAll();
//
//        int created = 0;
//
//        /*
//         * Each freelancer applies to several random jobs.
//         */
//        for (Freelancer freelancer : freelancers) {
//
//            int applicationCount =
//                    1 + random.nextInt(5);
//
//            List<Job> shuffledJobs =
//                    new ArrayList<>(jobs);
//
//            Collections.shuffle(shuffledJobs);
//
//            int max =
//                    Math.min(
//                            applicationCount,
//                            shuffledJobs.size()
//                    );
//
//            for (int i = 0; i < max; i++) {
//
//                Job job = shuffledJobs.get(i);
//
//                if (applicationRepo
//                        .existsByFreelancerAndJob(
//                                freelancer,
//                                job)) {
//
//                    continue;
//                }
//
//                Application application =
//                        new Application();
//
//                application.setJob(job);
//                application.setFreelancer(freelancer);
//
//                application.setCurriculumVitae(
//                        "Example_CV.pdf"
//                );
//
//                application.setCoverLetter(
//                        "Dear Hiring Manager,\n\n" +
//                        "I am interested in this position and " +
//                        "believe that my skills and experience " +
//                        "would make me a strong candidate for " +
//                        "this opportunity.\n\n" +
//                        "Thank you for considering my application."
//                );
//
//                application.setStatus(
//                        randomApplicationStatus()
//                );
//
//                application.setCreatedAt(
//                        LocalDateTime.now()
//                                .minusDays(
//                                        random.nextInt(20)
//                                )
//                );
//
//                applicationRepo.save(
//                        application
//                );
//
//                created++;
//            }
//        }
//
//        System.out.println(
//                "Applications created: " + created
//        );
//    }
//
//
//    // =========================================================
//    // ENGAGEMENTS
//    // =========================================================
//
//    private void seedEngagements() {
//
//        List<Employer> employers =
//                employerRepo.findAll();
//
//        List<Freelancer> freelancers =
//                freelancerRepo.findAll();
//
//        List<Category> categories =
//                categoryRepo.findAll();
//
//        if (employers.isEmpty()
//                || freelancers.isEmpty()
//                || categories.isEmpty()) {
//
//            System.out.println(
//                    "Cannot create engagements."
//            );
//
//            return;
//        }
//
//        /*
//         * Create around 50 engagements.
//         */
//        int engagementCount = 50;
//
//        int created = 0;
//
//        for (int i = 1; i <= engagementCount; i++) {
//
//            Employer employer =
//                    employers.get(
//                            random.nextInt(
//                                    employers.size()
//                            )
//                    );
//
//            Freelancer freelancer =
//                    freelancers.get(
//                            random.nextInt(
//                                    freelancers.size()
//                            )
//                    );
//
//            Category category =
//                    categories.get(
//                            random.nextInt(
//                                    categories.size()
//                            )
//                    );
//
//            Engagement engagement =
//                    new Engagement();
//
//            engagement.setTitle(
//                    engagementTitle()
//            );
//
//            engagement.setDescription(
//                    "This engagement involves working with " +
//                    "the client on an exciting project. " +
//                    "The freelancer will be responsible for " +
//                    "delivering high-quality work and meeting " +
//                    "project requirements."
//            );
//
//            engagement.setMonthlyRate(
//                    BigDecimal.valueOf(
//                            500000 +
//                            random.nextInt(1500000)
//                    )
//            );
//
//            engagement.setStartDate(
//                    LocalDateTime.now()
//                            .minusDays(
//                                    random.nextInt(30)
//                            )
//            );
//
//            engagement.setEndDate(
//                    LocalDateTime.now()
//                            .plusDays(
//                                    30 +
//                                    random.nextInt(120)
//                            )
//            );
//
//            engagement.setEmployer(employer);
//            engagement.setFreelancer(freelancer);
//            engagement.setCategory(category);
//
//            engagement.setStatus(
//                    randomEngagementStatus()
//            );
//
//            engagementRepo.save(
//                    engagement
//            );
//
//            created++;
//        }
//
//        System.out.println(
//                "Engagements created: " + created
//        );
//    }
//
//
//    // =========================================================
//    // RANDOM ENUM HELPERS
//    // =========================================================
//
//    private EmploymentType randomEmploymentType() {
//
//        EmploymentType[] values =
//                EmploymentType.values();
//
//        return values[
//                random.nextInt(values.length)
//        ];
//    }
//
//
//    private DurationType randomDuration() {
//
//        DurationType[] values =
//                DurationType.values();
//
//        return values[
//                random.nextInt(values.length)
//        ];
//    }
//
//
//    private ExperienceLevel randomExperienceLevel() {
//
//        ExperienceLevel[] values =
//                ExperienceLevel.values();
//
//        return values[
//                random.nextInt(values.length)
//        ];
//    }
//
//
//    private JobStatus randomJobStatus() {
//
//        JobStatus[] values =
//                JobStatus.values();
//
//        return values[
//                random.nextInt(values.length)
//        ];
//    }
//
//
//    private ApplicationStatus randomApplicationStatus() {
//
//        ApplicationStatus[] values =
//                ApplicationStatus.values();
//
//        return values[
//                random.nextInt(values.length)
//        ];
//    }
//
//
//    private EngagementStatus randomEngagementStatus() {
//
//        EngagementStatus[] values =
//                EngagementStatus.values();
//
//        return values[
//                random.nextInt(values.length)
//        ];
//    }
//
//
//    private String engagementTitle() {
//
//        String[] titles = {
//                "Website Development Project",
//                "Mobile Application Project",
//                "UI/UX Design Project",
//                "Data Analysis Project",
//                "E-commerce Development",
//                "Business Website Development",
//                "Software Development Project",
//                "Digital Marketing Project",
//                "Database Development Project",
//                "Cloud Migration Project"
//        };
//
//        return titles[
//                random.nextInt(titles.length)
//        ];
//    }
//}