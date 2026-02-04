// Minimal supabase stub to satisfy imports in dev environment
export const supabase = {
  from: (tableName: string) => ({
    select: async (..._args: any[]) => ({ data: [], error: null }),
    insert: async (_data: any) => ({ data: [], error: null }),
    update: async (_data: any) => ({ data: [], error: null }),
  }),
  auth: {
    getUser: async () => ({ data: null, error: null }),
    // getSession should return an object with a `data` property containing `session`
    getSession: async () => ({ data: { session: null }, error: null }),
    // keep compatibility with other auth helpers if needed
    signIn: async () => ({ data: null, error: null }),
    signOut: async () => ({ data: null, error: null })
  }
};

export default supabase;
