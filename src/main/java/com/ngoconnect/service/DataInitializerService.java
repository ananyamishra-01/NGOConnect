package com.ngoconnect.service;

import com.ngoconnect.model.*;
import com.ngoconnect.repository.OrganisationRepository;
import com.ngoconnect.repository.UserRepository;

import java.time.LocalDate;

public class DataInitializerService {

    private static final OrganisationRepository orgRepo = new OrganisationRepository();
    private static final UserRepository userRepo = new UserRepository();

    public static OrganisationRepository getOrgRepo()   { return orgRepo; }
    public static UserRepository getUserRepo()           { return userRepo; }

    public static void initialize() {
        if (orgRepo.count() > 0) return; // already seeded

        seedOrganisations();
        seedUsers();
        System.out.println("  Sample data loaded successfully.\n");
    }

    private static void seedOrganisations() {

        // 1. HelpAge India
        Organisation o1 = new Organisation(
                "HelpAge India",
                "One of India's leading NGOs working for the cause and care of disadvantaged elderly. "
              + "Runs mobile healthcare units, elder helplines, and senior skill centres.",
                OrgType.NGO,
                "New Delhi", "Delhi",
                new ContactInfo("+91-11-41688955", "contact@helpageindia.org",
                        "www.helpageindia.org", "C-14, Qutab Institutional Area, New Delhi"),
                1978, "Elder Care, Mobile Healthcare, Skill Training");
        o1.addCategory(Category.OLD_AGE_SUPPORT);
        o1.addCategory(Category.HEALTH);
        o1.setVerified(true);
        o1.setRegistrationNumber("DL/NGO/1978/004");
        o1.setVolunteerCount(12000);
        o1.addRecentActivity("Launched 'Elder Helpline 1800-180-1253' across 5 states");
        o1.addRecentActivity("Mobile healthcare vans served 85,000 elderly in rural Bihar");
        o1.addEvent(new Event("Silver Volunteers Drive", "Recruit and train volunteers for elder care",
                LocalDate.now().plusDays(15), "Delhi NCR", Event.EventType.DRIVE));
        o1.addReview(new Review("Priya Sharma", 5, "Wonderful work for the elderly. Highly professional."));
        o1.addReview(new Review("Rahul Verma", 4, "Great initiative. Could expand to more rural areas."));
        orgRepo.add(o1);

        // 2. Mahila Samakhya
        Organisation o2 = new Organisation(
                "Mahila Samakhya Collective",
                "A grassroots SHG network empowering rural women through education, legal literacy, "
              + "and livelihood support. Operates in 10 districts across Uttar Pradesh.",
                OrgType.SHG,
                "Lucknow", "Uttar Pradesh",
                new ContactInfo("+91-522-2308741", "mahilasamakhya.up@gmail.com",
                        "N/A", "14 Hazratganj, Lucknow, UP"),
                2003, "Women Empowerment, Legal Aid, Micro-Finance");
        o2.addCategory(Category.WOMEN_EMPOWERMENT);
        o2.addCategory(Category.LIVELIHOOD);
        o2.setVerified(true);
        o2.setRegistrationNumber("UP/SHG/2003/117");
        o2.setVolunteerCount(3400);
        o2.addRecentActivity("Trained 2,000 women in digital financial literacy");
        o2.addRecentActivity("Filed 150 domestic violence cases through legal aid cell");
        o2.addEvent(new Event("Women Entrepreneurship Workshop", "Business skills for rural women",
                LocalDate.now().plusDays(8), "Varanasi", Event.EventType.WORKSHOP));
        o2.addReview(new Review("Anita Devi", 5, "This organisation changed my life. I now run my own business."));
        o2.addReview(new Review("Smita Joshi", 4, "Excellent legal support. Very responsive team."));
        orgRepo.add(o2);

        // 3. CRY - Child Rights and You
        Organisation o3 = new Organisation(
                "CRY — Child Rights and You",
                "CRY works to ensure lasting change in the lives of underprivileged children by "
              + "supporting grassroots organisations and advocating for children's rights across India.",
                OrgType.NGO,
                "Mumbai", "Maharashtra",
                new ContactInfo("+91-22-23887801", "info@cry.org",
                        "www.cry.org", "CRY House, 189 Andheri-Kurla Road, Mumbai"),
                1979, "Child Education, Child Labour Abolition, Child Health");
        o3.addCategory(Category.CHILD_WELFARE);
        o3.addCategory(Category.EDUCATION);
        o3.setVerified(true);
        o3.setRegistrationNumber("MH/NGO/1979/022");
        o3.setVolunteerCount(50000);
        o3.addRecentActivity("Reached 3 million children through 'Right to Education' campaign");
        o3.addRecentActivity("Rescued 1,200 children from child labour in Tamil Nadu");
        o3.addEvent(new Event("Children's Day Awareness March", "Walk for children's rights",
                LocalDate.now().plusDays(40), "Mumbai", Event.EventType.AWARENESS));
        o3.addReview(new Review("Deepak Nair", 5, "CRY is doing incredible work on the ground."));
        orgRepo.add(o3);

        // 4. Wildlife SOS
        Organisation o4 = new Organisation(
                "Wildlife SOS",
                "Dedicated to rescuing and rehabilitating wildlife in distress across India. "
              + "Operates rescue centres, anti-poaching initiatives, and community awareness programmes.",
                OrgType.NGO,
                "Agra", "Uttar Pradesh",
                new ContactInfo("+91-11-41021000", "info@wildlifesos.org",
                        "www.wildlifesos.org", "Plot 20, Sector 4, IMT Manesar, Gurugram"),
                1995, "Animal Rescue, Wildlife Rehabilitation, Anti-Poaching");
        o4.addCategory(Category.ANIMAL_WELFARE);
        o4.addCategory(Category.ENVIRONMENTAL_CONSERVATION);
        o4.setVerified(true);
        o4.setRegistrationNumber("UP/NGO/1995/031");
        o4.setVolunteerCount(8200);
        o4.addRecentActivity("Rescued 22 leopards from human-wildlife conflict zones");
        o4.addRecentActivity("Freed the last dancing bear in India — a 20-year mission completed");
        o4.addEvent(new Event("Wildlife Photography Workshop", "Learn wildlife photography & conservation",
                LocalDate.now().plusDays(20), "Agra Bear Rescue Facility", Event.EventType.WORKSHOP));
        o4.addReview(new Review("Kiran Patel", 5, "A must-visit. The bear rescue facility is phenomenal."));
        orgRepo.add(o4);

        // 5. Chintan Environmental Research
        Organisation o5 = new Organisation(
                "Chintan Environmental Research and Action Group",
                "Works on waste management, climate justice, and environmental policy. Champions the "
              + "rights of waste workers (ragpickers) and promotes zero-waste communities.",
                OrgType.SOCIETY,
                "New Delhi", "Delhi",
                new ContactInfo("+91-11-27622379", "chintan@chintan-india.org",
                        "www.chintan-india.org", "G-5, Lajpat Nagar III, New Delhi"),
                1999, "Waste Management, Climate Policy, Green Livelihoods");
        o5.addCategory(Category.ENVIRONMENTAL_CONSERVATION);
        o5.setVerified(true);
        o5.setRegistrationNumber("DL/SOC/1999/088");
        o5.setVolunteerCount(2100);
        o5.addRecentActivity("Diverted 5,000 tonnes of waste from Delhi landfills in 2023");
        o5.addRecentActivity("Trained 800 ragpickers in source segregation and safe handling");
        o5.addEvent(new Event("Zero Waste Community Drive", "Neighbourhood waste segregation initiative",
                LocalDate.now().plusDays(5), "South Delhi", Event.EventType.DRIVE));
        o5.addReview(new Review("Tanvi Mehra", 5, "Chintan's work on waste worker rights is pioneering."));
        o5.addReview(new Review("Arjun Kapoor", 4, "Very impactful. Great awareness sessions."));
        orgRepo.add(o5);

        // 6. Samarthan — Disabled Persons
        Organisation o6 = new Organisation(
                "Samarthan — Centre for Disability",
                "Promotes inclusion, rights, and rehabilitation of persons with disabilities. "
              + "Provides assistive technology, vocational training, and legal advocacy.",
                OrgType.TRUST,
                "Bhopal", "Madhya Pradesh",
                new ContactInfo("+91-755-2550822", "info@samarthan.org",
                        "www.samarthan.org", "E-8, Arera Colony, Bhopal, MP"),
                2001, "Disability Rights, Assistive Technology, Inclusive Education");
        o6.addCategory(Category.SPECIALLY_ABLED);
        o6.addCategory(Category.EDUCATION);
        o6.setVerified(true);
        o6.setRegistrationNumber("MP/TRUST/2001/045");
        o6.setVolunteerCount(1800);
        o6.addRecentActivity("Distributed 500 motorised wheelchairs in tribal areas of MP");
        o6.addRecentActivity("Trained 300 teachers in inclusive classroom methodologies");
        o6.addEvent(new Event("Assistive Tech Expo", "Showcase of affordable assistive devices",
                LocalDate.now().plusDays(30), "Bhopal", Event.EventType.AWARENESS));
        o6.addReview(new Review("Meera Iyer", 5, "My brother received a wheelchair and vocational training. Life-changing."));
        orgRepo.add(o6);

        // 7. Teach For India
        Organisation o7 = new Organisation(
                "Teach For India",
                "Places passionate college graduates and professionals as full-time teachers "
              + "in under-resourced schools to ensure every child receives an excellent education.",
                OrgType.FOUNDATION,
                "Pune", "Maharashtra",
                new ContactInfo("+91-20-66047000", "contact@teachforindia.org",
                        "www.teachforindia.org", "5th Floor, Tower B, Kalyani Nagar, Pune"),
                2009, "Quality Education, Leadership Development, Urban Schools");
        o7.addCategory(Category.EDUCATION);
        o7.addCategory(Category.CHILD_WELFARE);
        o7.setVerified(true);
        o7.setRegistrationNumber("MH/FNDTN/2009/011");
        o7.setVolunteerCount(4600);
        o7.addRecentActivity("1,100 Fellows placed in government schools across 9 cities");
        o7.addRecentActivity("40,000 children impacted by Teach For India Fellows in 2023-24");
        o7.addEvent(new Event("Fellowship Info Session", "Apply to become a Teach For India Fellow",
                LocalDate.now().plusDays(12), "Online + Delhi Hub", Event.EventType.AWARENESS));
        o7.addReview(new Review("Rohan Sinha", 5, "Being a Fellow changed my perspective entirely. Highly recommend."));
        o7.addReview(new Review("Leena Gupta", 4, "Rigorous but deeply rewarding programme."));
        orgRepo.add(o7);

        // 8. Jal Saheli Network (SHG)
        Organisation o8 = new Organisation(
                "Jal Saheli Water Network",
                "A women-led SHG network managing water and sanitation at village level in Bundelkhand. "
              + "Trains women to become water monitors and repair handpumps.",
                OrgType.SHG,
                "Jhansi", "Uttar Pradesh",
                new ContactInfo("+91-9876501234", "jalsaheli.network@gmail.com",
                        "N/A", "Village Patha, Block Moth, Jhansi, UP"),
                2007, "Clean Water, Sanitation, Women Leadership");
        o8.addCategory(Category.CLEAN_WATER);
        o8.addCategory(Category.WOMEN_EMPOWERMENT);
        o8.setVolunteerCount(950);
        o8.addRecentActivity("Restored 120 defunct handpumps across 30 villages");
        o8.addRecentActivity("Led campaigns to end open defecation in 18 gram panchayats");
        o8.addReview(new Review("Vandana Singh", 5, "Jal Sahelis are the backbone of water security in Bundelkhand."));
        orgRepo.add(o8);

        // 9. SEWA — Self Employed Women's Association
        Organisation o9 = new Organisation(
                "SEWA — Self Employed Women's Association",
                "A trade union and SHG federation of poor, self-employed women workers in the informal "
              + "economy. Provides banking, insurance, childcare, and legal support.",
                OrgType.SHG,
                "Ahmedabad", "Gujarat",
                new ContactInfo("+91-79-25506444", "sewa@sewa.org",
                        "www.sewa.org", "SEWA Reception Centre, Bhadra, Ahmedabad"),
                1972, "Women's Economic Empowerment, Informal Labour, Micro-Finance");
        o9.addCategory(Category.WOMEN_EMPOWERMENT);
        o9.addCategory(Category.LIVELIHOOD);
        o9.setVerified(true);
        o9.setRegistrationNumber("GJ/SHG/1972/001");
        o9.setVolunteerCount(20000);
        o9.addRecentActivity("2 million member milestone crossed in 2023");
        o9.addRecentActivity("SEWA Bank disbursed ₹420 crore in micro-loans to women artisans");
        o9.addEvent(new Event("SEWA Bazaar Annual Fair", "Showcase and sell handmade products by SEWA members",
                LocalDate.now().plusDays(60), "Ahmedabad Exhibition Ground", Event.EventType.FUNDRAISER));
        o9.addReview(new Review("Bhavna Patel", 5, "SEWA gave me financial independence. Truly life-altering."));
        orgRepo.add(o9);

        // 10. Green Yatra
        Organisation o10 = new Organisation(
                "Green Yatra",
                "India's largest tree-plantation and urban greening NGO. Runs school eco-clubs, "
              + "mangrove restoration drives, and corporate green volunteering programmes.",
                OrgType.NGO,
                "Mumbai", "Maharashtra",
                new ContactInfo("+91-22-28911882", "contact@greenyatra.org",
                        "www.greenyatra.org", "Plot 7B, Chakala Industrial Area, Andheri East, Mumbai"),
                2008, "Urban Greening, Mangrove Restoration, School Eco-Clubs");
        o10.addCategory(Category.ENVIRONMENTAL_CONSERVATION);
        o10.setVerified(true);
        o10.setRegistrationNumber("MH/NGO/2008/055");
        o10.setVolunteerCount(35000);
        o10.addRecentActivity("Planted 2 million trees across Maharashtra in 2023");
        o10.addRecentActivity("Restored 400 hectares of mangrove forests in Mumbai coastline");
        o10.addEvent(new Event("Monsoon Plantation Drive", "Plant 10,000 saplings with community volunteers",
                LocalDate.now().plusDays(3), "Aarey Forest, Mumbai", Event.EventType.DRIVE));
        o10.addReview(new Review("Siddharth Rao", 5, "Organised, impactful and very welcoming to new volunteers."));
        o10.addReview(new Review("Pallavi Nair", 4, "Great programme for school students too."));
        orgRepo.add(o10);

        // 11. iCall — Mental Health Helpline
        Organisation o11 = new Organisation(
                "iCall — Psychosocial Support Programme",
                "A professional counselling and mental health service offering individual therapy, "
              + "crisis support, and training for health workers. Run by TISS, Mumbai.",
                OrgType.SOCIETY,
                "Mumbai", "Maharashtra",
                new ContactInfo("9152987821", "icall@tiss.edu",
                        "icallhelpline.org", "Tata Institute of Social Sciences, V.N. Purav Marg, Mumbai"),
                2012, "Mental Health, Crisis Counselling, Psychosocial Support");
        o11.addCategory(Category.HEALTH);
        o11.addCategory(Category.SPECIALLY_ABLED);
        o11.setVerified(true);
        o11.setVolunteerCount(600);
        o11.addRecentActivity("Handled 12,000+ calls on mental health helpline in 2023");
        o11.addRecentActivity("Trained 500 counsellors in low-resource community settings");
        o11.addEvent(new Event("Mental Health First Aid Training", "2-day certified training for community workers",
                LocalDate.now().plusDays(22), "TISS Mumbai Campus", Event.EventType.TRAINING));
        o11.addReview(new Review("Nisha Kulkarni", 5, "The counsellors are empathetic and highly professional."));
        orgRepo.add(o11);

        // 12. Goonj
        Organisation o12 = new Organisation(
                "Goonj",
                "Converts urban surplus (clothes, material) into a development resource for rural India. "
              + "Uses cloth as currency to motivate community-led infrastructure work.",
                OrgType.NGO,
                "New Delhi", "Delhi",
                new ContactInfo("+91-11-26972351", "mail@goonj.org",
                        "www.goonj.org", "J-93, Sarita Vihar, New Delhi"),
                1999, "Disaster Relief, Urban-Rural Exchange, Rural Infrastructure");
        o12.addCategory(Category.RURAL_DEVELOPMENT);
        o12.addCategory(Category.DISASTER_RELIEF);
        o12.setVerified(true);
        o12.setRegistrationNumber("DL/NGO/1999/066");
        o12.setVolunteerCount(75000);
        o12.addRecentActivity("Processed and distributed 1,200 tonnes of material to flood-hit Assam");
        o12.addRecentActivity("Cloth for Work initiative built 900 km of rural pathways");
        o12.addEvent(new Event("Urban Collection Drive", "Donate usable clothes and materials",
                LocalDate.now().plusDays(7), "Pan-India Drop Points", Event.EventType.DRIVE));
        o12.addReview(new Review("Vikram Mehta", 5, "Brilliant concept. Dignity in giving and receiving."));
        o12.addReview(new Review("Sunita Roy", 5, "Goonj is what grassroots development should look like."));
        orgRepo.add(o12);
    }

    private static void seedUsers() {
        try {
            UserService us = new UserService(userRepo);
            // Admin account
            us.register("admin", "admin@ngoconnect.in", "Admin@123", User.UserRole.ADMIN);
            // Sample volunteer
            User vol = us.register("ananya", "ananya@example.com", "Pass@123", User.UserRole.VOLUNTEER);
            vol.addInterest(Category.WOMEN_EMPOWERMENT);
            vol.addInterest(Category.EDUCATION);
            userRepo.update(vol);
        } catch (Exception ignored) {} // silently skip if already exists
    }
}
