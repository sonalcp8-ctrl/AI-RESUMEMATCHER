import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { skillApi } from '../services/api';
import {
  Award,
  Plus,
  Trash2,
  Search,
  Sparkles,
  CheckCircle,
  Tag,
  AlertCircle,
} from 'lucide-react';

const Skills = () => {
  const { user, refreshUser } = useAuth();
  const [allSkills, setAllSkills] = useState([]);
  const [userSkills, setUserSkills] = useState([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [customSkillName, setCustomSkillName] = useState('');
  const [customCategory, setCustomCategory] = useState('Programming');
  const [toast, setToast] = useState(null);

  const loadSkills = async () => {
    setLoading(true);
    try {
      const [allRes, userRes] = await Promise.all([
        skillApi.getAllSkills(),
        user?.id ? skillApi.getUserSkills(user.id) : Promise.resolve({ data: { data: [] } }),
      ]);

      if (allRes.data?.success) setAllSkills(allRes.data.data);
      if (userRes.data?.success) setUserSkills(userRes.data.data);
    } catch (err) {
      console.error('Failed to load skills', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadSkills();
  }, [user?.id]);

  const showToast = (message, type = 'success') => {
    setToast({ message, type });
    setTimeout(() => setToast(null), 3000);
  };

  const handleAddExisting = async (skill) => {
    if (!user) return;
    try {
      const res = await skillApi.addSkillToUser(user.id, { skillId: skill.id });
      if (res.data?.success) {
        setUserSkills(res.data.data);
        showToast(`Added skill: ${skill.skillName}`);
        refreshUser(user.id);
      }
    } catch (err) {
      showToast(err.response?.data?.message || 'Failed to add skill', 'error');
    }
  };

  const handleRemove = async (skillId, skillName) => {
    if (!user) return;
    try {
      const res = await skillApi.removeSkillFromUser(user.id, skillId);
      if (res.data?.success) {
        setUserSkills(res.data.data);
        showToast(`Removed ${skillName || 'skill'}`);
        refreshUser(user.id);
      }
    } catch (err) {
      showToast('Failed to remove skill', 'error');
    }
  };

  const handleAddCustom = async (e) => {
    e.preventDefault();
    if (!customSkillName.trim() || !user) return;

    try {
      const res = await skillApi.addSkillToUser(user.id, {
        skillName: customSkillName.trim(),
        category: customCategory,
      });
      if (res.data?.success) {
        setUserSkills(res.data.data);
        setCustomSkillName('');
        showToast(`Added custom skill: ${customSkillName}`);
        refreshUser(user.id);
        loadSkills(); // reload all skills
      }
    } catch (err) {
      showToast(err.response?.data?.message || 'Failed to add custom skill', 'error');
    }
  };

  const userSkillIds = new Set(userSkills.map((s) => s.id));

  // Available skills not yet in user skills
  const availableSkills = allSkills.filter(
    (s) =>
      !userSkillIds.has(s.id) &&
      (s.skillName.toLowerCase().includes(search.toLowerCase()) ||
        (s.category && s.category.toLowerCase().includes(search.toLowerCase())))
  );

  // Group user skills by category
  const groupedUserSkills = userSkills.reduce((acc, s) => {
    const cat = s.category || 'General';
    if (!acc[cat]) acc[cat] = [];
    acc[cat].push(s);
    return acc;
  }, {});

  return (
    <div className="page-container">
      <div className="page-header">
        <div>
          <h2>
            Manage Your Skills <Sparkles className="icon-accent" size={24} />
          </h2>
          <p>
            Add your technical and soft skills. These directly calculate your job match percentage.
          </p>
        </div>
      </div>

      {toast && (
        <div className={`notification-toast ${toast.type === 'success' ? 'toast-success' : 'toast-error'}`}>
          {toast.type === 'success' ? <CheckCircle size={18} /> : <AlertCircle size={18} />}
          <span>{toast.message}</span>
        </div>
      )}

      <div className="skills-layout">
        {/* Left: Your Current Skills */}
        <div className="skills-main-col">
          <div className="card">
            <div className="card-header">
              <div className="header-with-badge">
                <h3>My Current Profile Skills</h3>
                <span className="count-pill">{userSkills.length} Total</span>
              </div>
            </div>

            {loading ? (
              <div className="loading-state">
                <div className="spinner"></div>
              </div>
            ) : userSkills.length === 0 ? (
              <div className="empty-state">
                <Award size={48} className="empty-icon" />
                <h3>No skills added yet</h3>
                <p>Select from the database on the right or type a custom skill below to begin.</p>
              </div>
            ) : (
              <div className="user-skills-grouped">
                {Object.entries(groupedUserSkills).map(([category, skills]) => (
                  <div key={category} className="category-group">
                    <h4 className="category-title">{category}</h4>
                    <div className="chips-container">
                      {skills.map((skill) => (
                        <div key={skill.id} className="skill-chip-interactive">
                          <span>{skill.skillName}</span>
                          <button
                            type="button"
                            className="btn-chip-delete"
                            title="Remove Skill"
                            onClick={() => handleRemove(skill.id, skill.skillName)}
                          >
                            <Trash2 size={14} />
                          </button>
                        </div>
                      ))}
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>

          {/* Add Custom Skill Form */}
          <div className="card mt-4">
            <div className="card-header">
              <h3>Add Custom Skill</h3>
            </div>
            <form onSubmit={handleAddCustom} className="custom-skill-form">
              <div className="form-group flex-2">
                <label>Skill Name</label>
                <input
                  type="text"
                  placeholder="e.g. Next.js, Kubernetes, Tailwind..."
                  value={customSkillName}
                  onChange={(e) => setCustomSkillName(e.target.value)}
                  required
                />
              </div>

              <div className="form-group flex-1">
                <label>Category</label>
                <select
                  value={customCategory}
                  onChange={(e) => setCustomCategory(e.target.value)}
                >
                  <option value="Programming">Programming</option>
                  <option value="Framework">Framework</option>
                  <option value="Database">Database</option>
                  <option value="Tools">Tools</option>
                  <option value="Web">Web</option>
                  <option value="AI/ML">AI/ML</option>
                  <option value="Mobile">Mobile</option>
                  <option value="Cloud">Cloud</option>
                  <option value="Other">Other</option>
                </select>
              </div>

              <button type="submit" className="btn-primary btn-align-bottom">
                <Plus size={18} /> Add
              </button>
            </form>
          </div>
        </div>

        {/* Right: Available Skills Library */}
        <div className="skills-sidebar-col">
          <div className="card">
            <div className="card-header">
              <h3>Skills Library</h3>
              <p className="card-subtitle">Click '+' on any skill to add it to your profile</p>
            </div>

            <div className="search-input-wrapper mb-3">
              <Search size={16} className="search-icon" />
              <input
                type="text"
                placeholder="Search database skills..."
                value={search}
                onChange={(e) => setSearch(e.target.value)}
              />
            </div>

            <div className="available-skills-list">
              {availableSkills.length === 0 ? (
                <p className="text-muted text-sm text-center py-4">
                  {allSkills.length === 0
                    ? 'Loading skill catalog...'
                    : 'All matching skills are already added!'}
                </p>
              ) : (
                availableSkills.map((s) => (
                  <div key={s.id} className="available-skill-item">
                    <div>
                      <span className="skill-item-name">{s.skillName}</span>
                      <span className="skill-item-cat">{s.category}</span>
                    </div>
                    <button
                      type="button"
                      className="btn-add-skill"
                      onClick={() => handleAddExisting(s)}
                      title="Add to profile"
                    >
                      <Plus size={16} />
                    </button>
                  </div>
                ))
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Skills;
