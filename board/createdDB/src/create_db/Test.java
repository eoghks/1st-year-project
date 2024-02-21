package create_db;

//postgres 7316
public class Test {

	public static void main(String[] args) {
		System.out.println("Super 계정 로그인");
		Pdb db = new Pdb();
		db.create_database();
		db.create_table();
	}
}
